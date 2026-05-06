package kr.co.mapspring.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /*
     * 현재 로그인한 유저의 정보를 조회합니다.
     * Authorization 헤더의 accessToken에서 userId를 추출합니다.
     */
    @GetMapping("/me")
    public ApiResponseDTO<UserDto.ResponseMe> getMe(HttpServletRequest request) {

        // Authorization 헤더에서 Bearer 토큰 추출
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7); // "Bearer " 제거

        // 토큰에서 userId 추출
        Long userId = jwtTokenProvider.getUserId(token);

        UserDto.ResponseMe response = userService.getMe(userId);

        return ApiResponseDTO.success("내 정보 조회 성공", response);
    }
}