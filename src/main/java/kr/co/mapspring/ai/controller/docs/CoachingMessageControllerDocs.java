package kr.co.mapspring.ai.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "AI Coaching Message", description = "AI 코칭 메시지 API")
public interface CoachingMessageControllerDocs {

    @Operation(
            summary = "AI 코칭 메시지 저장",
            description = "AI 코칭 세션에 사용자 또는 AI 메시지를 저장한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "메시지 저장 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 메시지 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "코칭 세션 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<CoachingMessageDto.ResponseCoachingMessage>> saveCoachingMessage(
            CoachingMessageDto.RequestSaveCoachingMessage request
    );

    @Operation(
            summary = "AI 코칭 메시지 목록 조회",
            description = "코칭 세션 ID를 기준으로 저장된 AI 코칭 메시지 목록을 생성 시간 오름차순으로 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "메시지 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "코칭 세션 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<CoachingMessageDto.ResponseGetCoachingMessages>> getCoachingMessages(
            @Parameter(description = "AI 코칭 세션 ID", example = "100")
            @PathVariable ("CoachingSessionId") Long coachingSessionId
    );
}