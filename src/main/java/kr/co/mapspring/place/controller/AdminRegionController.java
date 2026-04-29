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
import kr.co.mapspring.place.controller.Docs.AdminRegionControllerDocs;
import kr.co.mapspring.place.dto.AdminCreateRegionDto;
import kr.co.mapspring.place.dto.AdminReadRegionDto;
import kr.co.mapspring.place.dto.AdminRegionListDto;
import kr.co.mapspring.place.dto.AdminUpdateRegionDto;
import kr.co.mapspring.place.service.AdminRegionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/regions")
@RequiredArgsConstructor
public class AdminRegionController implements AdminRegionControllerDocs {

    private final AdminRegionService adminRegionService;

    // 지역 생성
    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> createRegion(@RequestBody AdminCreateRegionDto.RequestCreate request) {
        adminRegionService.createRegion(request);
        return ResponseEntity.ok(ApiResponseDTO.success("지역 생성 완료"));
    }

    // 지역 상세 조회
    @Override
    @GetMapping("/{regionId}")
    public ResponseEntity<ApiResponseDTO<AdminReadRegionDto.ResponseRead>> readRegionDetail(@PathVariable("regionId") Long regionId) {
        AdminReadRegionDto.ResponseRead result =adminRegionService.readRegion(regionId);
        return ResponseEntity.ok(ApiResponseDTO.success("지역 조회 완료", result));
    }

    // 지역 리스트 조회
    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminRegionListDto.ResponseList>>> readRegionList(@RequestParam(name = "keyword", required = false) String keyword) {
        List<AdminRegionListDto.ResponseList> result =adminRegionService.regionList(keyword);
        return ResponseEntity.ok(ApiResponseDTO.success("지역 리스트 조회 완료", result));
    }

    // 지역 수정
    @Override
    @PatchMapping("/{regionId}")
    public ResponseEntity<ApiResponseDTO<Void>> updateRegion(@PathVariable("regionId") Long regionId, 
    														 @RequestBody AdminUpdateRegionDto.RequestUpdate request) {
        adminRegionService.updateRegion(regionId, request);
        return ResponseEntity.ok(ApiResponseDTO.success("지역 수정 완료"));
    }

    // 지역 삭제
    @Override
    @DeleteMapping("/{regionId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRegion(@PathVariable("regionId") Long regionId) {
        adminRegionService.deleteRegion(regionId);
        return ResponseEntity.ok(ApiResponseDTO.success("지역 삭제 완료"));
    }
}