package kr.co.mapspring.place.controller.Docs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;

@Tag(name = "Admin Scenario", description = "관리자 시나리오 CRUD API")
public interface AdminScenarioControllerDocs {
	
	 @Operation(
	            summary = "시나리오 상세 조회",
	            description = "scenarioId를 기준으로 시나리오 상세 정보를 조회한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "조회 성공",
	                    content = @Content(schema = @Schema(implementation = AdminReadScenarioDto.ResponseRead.class))),
	            @ApiResponse(responseCode = "404", description = "시나리오를 찾을 수 없음")
	    })
	public ResponseEntity<ApiResponseDTO<AdminReadScenarioDto.ResponseRead>> readScenarioDetail(@PathVariable Long scenarioId); 
	
	 
	 @Operation(
	            summary = "시나리오 리스트 조회 및 검색",
	            description = "keyword가 없으면 전체 조회, 있으면 카테고리 또는 시나리오 기준으로 검색한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "조회 성공",
	                    content = @Content(schema = @Schema(implementation = AdminScenarioListDto.ResponseList.class)))
	    })
	public ResponseEntity<ApiResponseDTO<List<AdminScenarioListDto.ResponseList>>> readScenarioList(@RequestParam(required = false) String keyword);
	
	 
	 @Operation(
	            summary = "시나리오 수정",
	            description = "scenarioId를 기준으로 시나리오 정보를 수정한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "수정 성공"),
	            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
	            @ApiResponse(responseCode = "404", description = "시나리오를 찾을 수 없음")
	    })
	public ResponseEntity<ApiResponseDTO<Void>> updateScenario(@PathVariable Long scenarioId, 
															   @RequestBody AdminUpdateScenarioDto.RequestUpdate request);
	
	 
	 @Operation(
	            summary = "시나리오 생성",
	            description = "새로운 시나리오를 생성한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "생성 성공"),
	            @ApiResponse(responseCode = "400", description = "잘못된 요청 값")
	    })
	public ResponseEntity<ApiResponseDTO<Void>> createScenario(@RequestBody AdminCreateScenarioDto.RequestCreate request);
	
	 
	 @Operation(
	            summary = "시나리오 삭제",
	            description = "scenarioId를 기준으로 시나리오를 삭제한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "삭제 성공"),
	            @ApiResponse(responseCode = "404", description = "시나리오를 찾을 수 없음")
	    })
	public ResponseEntity<ApiResponseDTO<Void>> deleteScenario(@PathVariable Long scenarioId);

}
