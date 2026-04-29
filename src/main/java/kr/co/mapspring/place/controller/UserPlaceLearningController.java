package kr.co.mapspring.place.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.service.UserPlaceLearningService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class UserPlaceLearningController {
	
	private final UserPlaceLearningService userPlaceLearningService;
	
	@GetMapping("/{placeId}")
	public ResponseEntity<ApiResponseDTO<UserReadPlaceDto.ResponseRead>> clickPlace (@PathVariable("placeId") Long placeId) {
		
		UserReadPlaceDto.ResponseRead response = userPlaceLearningService.markerDetail(placeId);
		
		return ResponseEntity.ok(ApiResponseDTO.success("마커 상세 정보 조회 성공", response));
	}
	
	@PostMapping("/{placeId}/learningSession")
	public ResponseEntity<ApiResponseDTO<UserCreateLearningSessionDto.ResponseCreate>> learningStart (
			@PathVariable("placeId") Long placeId,
			@RequestBody UserCreateLearningSessionDto.RequestCreate request) {
		
		UserCreateLearningSessionDto.ResponseCreate response = userPlaceLearningService.learningStart(placeId, request);
		
		return ResponseEntity.ok(ApiResponseDTO.success("학습 세션 생성 성공", response));
	}

}
