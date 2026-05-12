package kr.co.mapspring.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.place.dto.UserRecentLearningPlaceDto;
import kr.co.mapspring.place.service.UserPlaceLearningService;
import kr.co.mapspring.user.controller.docs.UserControllerDocs;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserPlaceLearningService userPlaceLearningService;

    /*
     * 현재 로그인한 유저의 정보를 조회합니다.
     * Authorization 헤더의 accessToken에서 userId를 추출합니다.
     */
    @Override
    @GetMapping("/me")
    public ApiResponseDTO<UserDto.ResponseMe> getMe(HttpServletRequest request) {

    	 String authHeader = request.getHeader("Authorization");

    	    // ✅ 토큰 없으면 명확한 에러
    	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    	        throw new CustomException(ErrorCode.UNAUTHORIZED);
    	    }

    	    String token = authHeader.substring(7);
    	    Long userId = jwtTokenProvider.getUserId(token);
    	    UserDto.ResponseMe response = userService.getMe(userId);

    	    return ApiResponseDTO.success("내 정보 조회 성공", response);
    	}
    
    
    @Override
    @PatchMapping("/profile")
    public ApiResponseDTO<UserDto.ResponseProfileSetup> setupProfile(
            HttpServletRequest request,
            @Valid @RequestBody UserDto.RequestProfileSetup profileRequest
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        Long userId = jwtTokenProvider.getUserId(token);

        UserDto.ResponseProfileSetup response = userService.setupProfile(userId, profileRequest);

        return ApiResponseDTO.success("프로필 입력 성공", response);
    }
    
    @GetMapping("/profile/recentLearningPlaces")
	public ResponseEntity<ApiResponseDTO<List<UserRecentLearningPlaceDto.ResponseRecent>>> readRecentLearningPlaces(
	        @AuthenticationPrincipal User user
	) {
	    Long userId = user.getUserId();

	    List<UserRecentLearningPlaceDto.ResponseRecent> response =
	            userPlaceLearningService.readRecentLearningPlaces(userId);

	    return ResponseEntity.ok(
	            ApiResponseDTO.success("최근 학습 장소 조회 성공", response)
	    );
	}
    
    @Override
    @PatchMapping("/me")
    public ApiResponseDTO<UserDto.ResponseUpdateMe> updateMe(
            HttpServletRequest request,
            @Valid @RequestBody UserDto.RequestUpdateMe updateRequest) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        Long userId = jwtTokenProvider.getUserId(token);
        return ApiResponseDTO.success("내 정보 수정 성공", userService.updateMe(userId, updateRequest));
    }
    
    @Override
    @DeleteMapping("/me")
    public ApiResponseDTO<Void> deleteMe(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        Long userId = jwtTokenProvider.getUserId(token);
        userService.deleteMe(userId);
        return ApiResponseDTO.success("회원 탈퇴 성공", null);
    }
    
    
    
    @Override
    @PatchMapping("/me/password")
    public ApiResponseDTO<Void> changePassword(
            HttpServletRequest request,
            @Valid @RequestBody UserDto.RequestChangePassword passwordRequest) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        Long userId = jwtTokenProvider.getUserId(token);
        userService.changePassword(userId, passwordRequest);
        return ApiResponseDTO.success("비밀번호 변경 성공", null);
    }
    
    
    
}