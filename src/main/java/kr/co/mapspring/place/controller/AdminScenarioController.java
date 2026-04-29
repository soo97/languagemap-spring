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
import kr.co.mapspring.place.controller.Docs.AdminScenarioControllerDocs;
import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;
import kr.co.mapspring.place.service.AdminScenarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/content/scenarios")
@RequiredArgsConstructor
public class AdminScenarioController implements AdminScenarioControllerDocs {
	
	private final AdminScenarioService adminScenarioService;
	
	@Override
	@GetMapping("/{scenarioId}")
	public ResponseEntity<ApiResponseDTO<AdminReadScenarioDto.ResponseRead>> readScenarioDetail(@PathVariable("scenarioId") Long scenarioId) {
		AdminReadScenarioDto.ResponseRead scenarioDetail = adminScenarioService.readScenario(scenarioId);
		return ResponseEntity.ok(ApiResponseDTO.success("시나리오 상세 조회 완료", scenarioDetail));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<AdminScenarioListDto.ResponseList>>> readScenarioList(@RequestParam(name = "keyword", required = false) String keyword) {
		List<AdminScenarioListDto.ResponseList> scenarioList = adminScenarioService.scenarioList(keyword);
		return ResponseEntity.ok(ApiResponseDTO.success("시나리오 리스트 조회 완료", scenarioList));
	}
	
	@Override
	@PatchMapping("/{scenarioId}")
	public ResponseEntity<ApiResponseDTO<Void>> updateScenario(@PathVariable("scenarioId") Long scenarioId, 
															   @RequestBody AdminUpdateScenarioDto.RequestUpdate request) {
		adminScenarioService.updateScenario(scenarioId, request);
		return ResponseEntity.ok(ApiResponseDTO.success("시나리오 수정 완료"));
	}
	
	@Override
	@PostMapping
	public ResponseEntity<ApiResponseDTO<Void>> createScenario(@RequestBody AdminCreateScenarioDto.RequestCreate request) {
		adminScenarioService.createScenario(request);
		return ResponseEntity.ok(ApiResponseDTO.success("시나리오 생성 완료"));
	}
	
	@Override
	@DeleteMapping("/{scenarioId}")
	public ResponseEntity<ApiResponseDTO<Void>> deleteScenario(@PathVariable("scenarioId") Long scenarioId) {
		adminScenarioService.deleteScenario(scenarioId);
		return ResponseEntity.ok(ApiResponseDTO.success("시나리오 삭제 완료"));
	}

}
