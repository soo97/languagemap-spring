package kr.co.mapspring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StartCoachingWithInitialMessageDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "AI 코칭 시작 및 초기 메시지 생성 요청 DTO")
    public static class RequestStartCoachingWithInitialMessage {

        @Schema(description = "학습 세션 ID", example = "1")
        private Long sessionId;

        @Schema(description = "사용자가 선택한 코칭 옵션", example = "WORD")
        private String optionType;

        @Builder
        public RequestStartCoachingWithInitialMessage(Long sessionId, String optionType) {
            this.sessionId = sessionId;
            this.optionType = optionType;
        }
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 시작 및 초기 메시지 생성 응답 DTO")
    public static class ResponseStartCoachingWithInitialMessage {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "학습 세션 ID", example = "1")
        private Long sessionId;

        @Schema(description = "코칭 세션 상태", example = "RUNNING")
        private String coachingSessionStatus;

        @Schema(description = "사용자가 선택한 옵션", example = "WORD")
        private String selectedOption;

        @Schema(description = "현재 대화 턴 순서", example = "0")
        private Integer currentTurnOrder;

        @Schema(description = "AI가 처음 보내는 안내 메시지")
        private CoachingMessageDto.ResponseCoachingMessage initialMessage;
    }
}