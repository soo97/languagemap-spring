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
import kr.co.mapspring.place.controller.Docs.AdminPlaceControllerDocs;
import kr.co.mapspring.place.dto.AdminCreatePlaceDto;
import kr.co.mapspring.place.dto.AdminPlaceListDto;
import kr.co.mapspring.place.dto.AdminReadPlaceDto;
import kr.co.mapspring.place.dto.AdminUpdatePlaceDto;
import kr.co.mapspring.place.service.AdminPlaceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/places")
@RequiredArgsConstructor
public class AdminPlaceController implements AdminPlaceControllerDocs {
	
	private final AdminPlaceService adminPlaceService;
	
	@Override
	@PostMapping
	public ResponseEntity<ApiResponseDTO<Void>> createPlace(@RequestBody AdminCreatePlaceDto.RequestCreate request) {
		adminPlaceService.savePlace(request);
		return ResponseEntity.ok(ApiResponseDTO.success("장소 생성 완료"));
	}
	
	@Override
	@GetMapping("/{placeId}")
	public ResponseEntity<ApiResponseDTO<AdminReadPlaceDto.ResponseRead>> readPlaceDetail(@PathVariable("placeId") Long placeId) {
		AdminReadPlaceDto.ResponseRead placeDetail = adminPlaceService.readPlace(placeId);
		return ResponseEntity.ok(ApiResponseDTO.success("장소 조회 완료", placeDetail));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<AdminPlaceListDto.ResponseList>>> readPlaceList(@RequestParam(name = "placeName", required = false) String keyword) {
		List<AdminPlaceListDto.ResponseList> placeList = adminPlaceService.placeList(keyword);
		return ResponseEntity.ok(ApiResponseDTO.success("장소 리스트 조회 완료", placeList));
	}
	
	@Override
	@PatchMapping("/{placeId}")
	public ResponseEntity<ApiResponseDTO<Void>> updatePlace(@PathVariable("placeId") Long placeId, @RequestBody AdminUpdatePlaceDto.RequestUpdate request) {
		adminPlaceService.updatePlace(placeId, request);
		return ResponseEntity.ok(ApiResponseDTO.success("장소 수정 완료"));
	}
	
	@Override
	@DeleteMapping("/{placeId}")
	public ResponseEntity<ApiResponseDTO<Void>> deletePlace(@PathVariable("placeId") Long placeId) {
		adminPlaceService.deletePlace(placeId);
		return ResponseEntity.ok(ApiResponseDTO.success("장소 삭제 완료"));
	}
	
	

}
