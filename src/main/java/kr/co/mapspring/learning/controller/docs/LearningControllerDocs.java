package kr.co.mapspring.learning.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.learning.dto.LearningLogDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LearningControllerDocs {

    @Operation(
            summary = "학습 기록 조회",
            description = "사용자의 학습 기록과 점수 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "학습 기록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청값",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningLogDto.ResponseLog>>> getStudyLogs(Long userId);
}
