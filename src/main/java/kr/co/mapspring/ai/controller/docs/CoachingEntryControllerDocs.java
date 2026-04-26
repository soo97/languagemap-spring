package kr.co.mapspring.ai.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.ai.dto.CoachingEntryDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "AI Coaching Entry", description = "AI 코칭 진입 API")
public interface CoachingEntryControllerDocs {

    @Operation(
            summary = "AI 코칭 진입 데이터 조회",
            description = "지도 학습 세션 ID를 기준으로 장소 정보, 기존 학습 메시지, 평가 데이터를 조회한다.")
    
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "학습 세션 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class)))})
    
    ResponseEntity<ApiResponseDTO<CoachingEntryDto.ResponseGetCoachingEntry>> getCoachingEntryData(
            @Parameter(description = "지도 학습 세션 ID", example = "10")
            @PathVariable("sessionId") Long sessionId);
}