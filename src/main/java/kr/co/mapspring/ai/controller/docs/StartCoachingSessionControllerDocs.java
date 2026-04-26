package kr.co.mapspring.ai.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;

@Tag(name = "AI Coaching Session", description = "AI 코칭 세션 API")
public interface StartCoachingSessionControllerDocs {

    @Operation(
            summary = "AI 코칭 세션 시작",
            description = "지도 학습 세션 ID와 선택 옵션을 기준으로 AI 코칭 세션을 시작한다. 진행 중인 세션이 있으면 기존 세션을 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "코칭 세션 시작 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "학습 세션 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<StartCoachingSessionDto.ResponseStartCoachingSession>> startCoachingSession(
            StartCoachingSessionDto.RequestStartCoachingSession request
    );
}