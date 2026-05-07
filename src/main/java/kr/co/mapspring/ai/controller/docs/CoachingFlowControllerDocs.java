package kr.co.mapspring.ai.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.ai.dto.StartCoachingWithInitialMessageDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;

@Tag(name = "AI Coaching Flow", description = "AI 코칭 전체 흐름 API")
public interface CoachingFlowControllerDocs {

    @Operation(
            summary = "AI 코칭 시작 및 초기 메시지 생성",
            description = """
                    학습 세션을 기준으로 AI 코칭 세션을 시작하거나 기존 RUNNING 세션을 재사용합니다.
                    사용자가 선택한 optionType 기준으로 초기 안내 메시지를 생성하고,
                    해당 메시지를 첫 ASSISTANT 메시지로 저장한 뒤 반환합니다.
                    """
    )
    ResponseEntity<ApiResponseDTO<StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage>> startCoachingWithInitialMessage(
            StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request
    );
}