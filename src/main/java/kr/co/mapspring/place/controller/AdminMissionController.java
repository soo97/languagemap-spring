package kr.co.mapspring.place.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.place.dto.AdminCreateMissionDto;
import kr.co.mapspring.place.dto.AdminMissionListDto;
import kr.co.mapspring.place.dto.AdminReadMissionDto;
import kr.co.mapspring.place.dto.AdminUpdateMissionDto;
import kr.co.mapspring.place.service.AdminMissionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/content/mission")
@RequiredArgsConstructor
public class AdminMissionController {
	
	private final AdminMissionService adminMissionService;
	
	@GetMapping("/detail")
	public ResponseEntity<ApiResponseDTO<AdminReadMissionDto.ResponseRead>> readMissionDetail(@PathVariable Long missionId) {
		AdminReadMissionDto.ResponseRead missionDetail = adminMissionService.readMission(missionId);
		return ResponseEntity.ok(ApiResponseDTO.success("미션 상세 조회 완료", missionDetail));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<AdminMissionListDto.ResponseList>>> readMissionList(@RequestParam String keyword) {
		List<AdminMissionListDto.ResponseList> missionList = adminMissionService.missionList(keyword);
		return ResponseEntity.ok(ApiResponseDTO.success("미션 리스트 조회 완료", missionList));
	}
	
	@PatchMapping
	public ResponseEntity<ApiResponseDTO<Void>> updateMission(@PathVariable Long missionId, AdminUpdateMissionDto.RequestUpdate request) {
		adminMissionService.updateMission(missionId, request);
		return ResponseEntity.ok(ApiResponseDTO.success("미션 수정 완료"));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponseDTO<Void>> createMission(@RequestBody AdminCreateMissionDto.RequestCreate request) {
		adminMissionService.createMission(request);
		return ResponseEntity.ok(ApiResponseDTO.success("미션 생성 완료"));
	}
	
	@DeleteMapping
	public ResponseEntity<ApiResponseDTO<Void>> deleteMission(@PathVariable Long missionId) {
		adminMissionService.deleteMission(missionId);
		return ResponseEntity.ok(ApiResponseDTO.success("미션 삭제 완료"));
	}
}
