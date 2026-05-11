package kr.co.mapspring.place.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.controller.Docs.UserPlaceLearningControllerDocs;
import kr.co.mapspring.place.dto.UserChatDto;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserLearningProgressDto;
import kr.co.mapspring.place.dto.UserMissionCompleteDto;
import kr.co.mapspring.place.dto.UserMissionStartDto;
import kr.co.mapspring.place.dto.UserMissionStartDto.RequsetMissionStart;
import kr.co.mapspring.place.dto.UserPlaceListDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.dto.UserRegionListDto;
import kr.co.mapspring.place.service.UserPlaceLearningService;
import kr.co.mapspring.user.entity.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class UserPlaceLearningController implements UserPlaceLearningControllerDocs {
	
	private final UserPlaceLearningService userPlaceLearningService;
	
	@Override
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<UserPlaceListDto.ResponseList>>> readPlaceMarker() {
		
		List<UserPlaceListDto.ResponseList> response = userPlaceLearningService.readPlaceMarkers();

        return ResponseEntity.ok(ApiResponseDTO.success("장소 마커 목록 조회 성공", response));
	}
	
	@Override
	@GetMapping("/{placeId}")
	public ResponseEntity<ApiResponseDTO<UserReadPlaceDto.ResponseRead>> clickPlace(
			@PathVariable("placeId") Long placeId) {
		
		UserReadPlaceDto.ResponseRead response = userPlaceLearningService.markerDetail(placeId);
		
		return ResponseEntity.ok(ApiResponseDTO.success("마커 상세 정보 조회 성공", response));
	}
	
	@Override
	@PostMapping("/{placeId}/learningSessions")
	public ResponseEntity<ApiResponseDTO<UserCreateLearningSessionDto.ResponseCreate>> learningStart(
			@PathVariable("placeId") Long placeId,
			@RequestBody UserCreateLearningSessionDto.RequestCreate request) {
		
		UserCreateLearningSessionDto.ResponseCreate response = userPlaceLearningService.learningStart(placeId, request);
		
		return ResponseEntity.ok(ApiResponseDTO.success("학습 세션 및 미션 세션 생성 성공", response));
	}
	
	@Override
	@PatchMapping("/learningSessions/{sessionId}/missions/{missionId}")
	public ResponseEntity<ApiResponseDTO<UserMissionStartDto.ResponseMissionStart>> missionStart(
	        @PathVariable("sessionId") Long sessionId,
	        @PathVariable("missionId") Long missionId,
	        @RequestBody RequsetMissionStart request) {

	    UserMissionStartDto.ResponseMissionStart response =
	            userPlaceLearningService.missionStart(
	                    request.getUserId(),
	                    sessionId,
	                    missionId
	            );

	    return ResponseEntity.ok(ApiResponseDTO.success("미션 시작 완료", response));
	}
	
	@Override
	@PostMapping("/chat")
	public ResponseEntity<ApiResponseDTO<UserChatDto.ResponseChat>> chat(@RequestBody UserChatDto.RequestChat request) {
	    
		UserChatDto.ResponseChat response = userPlaceLearningService.chat(request);

	    return ResponseEntity.ok(ApiResponseDTO.success("AI 채팅 응답 성공", response));
	}
	
	@PatchMapping("/missionSessions/{sessionId}/missions/{missionId}")
	public ResponseEntity<ApiResponseDTO<UserMissionCompleteDto.ResponseComplete>> missionComplete(
	        @PathVariable("sessionId") Long sessionId,
	        @PathVariable("missionId") Long missionId,
	        @RequestBody UserMissionCompleteDto.RequestComplete request
	) {

	    UserMissionCompleteDto.ResponseComplete response =
	            userPlaceLearningService.missionComplete(
	                    request.getUserId(),
	                    sessionId,
	                    missionId
	            );

	    return ResponseEntity.ok(
	            ApiResponseDTO.success("미션 완료 처리 성공", response)
	    );
	}
	
	@Override
	@GetMapping("/me/progress")
	public ResponseEntity<ApiResponseDTO<List<UserLearningProgressDto.Response>>> readMyProgress(
	        @AuthenticationPrincipal User user
	) {
		
		if (user == null) {
	        throw new UserNotFoundException();
	    }
	    Long userId = user.getUserId();

	    List<UserLearningProgressDto.Response> response =
	            userPlaceLearningService.readMyProgress(userId);

	    return ResponseEntity.ok(
	            ApiResponseDTO.success("내 학습 진행 상황 조회 성공", response)
	    );
	}
	
	// 사용자 지역 리스트 조회
	@Override
	@GetMapping("/regions")
	public ResponseEntity<ApiResponseDTO<List<UserRegionListDto.ResponseList>>> readUserRegionList() {

	    List<UserRegionListDto.ResponseList> result = userPlaceLearningService.readRegionList();

	    return ResponseEntity.ok(ApiResponseDTO.success("사용자 지역 리스트 조회 완료", result));
	}
	
	

}
