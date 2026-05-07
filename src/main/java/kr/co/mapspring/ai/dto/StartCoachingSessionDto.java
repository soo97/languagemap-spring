package kr.co.mapspring.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StartCoachingSessionDto {
	
    @Getter
    @NoArgsConstructor
    @Schema(description = "AI 코칭 세션 시작 요청 DTO")
    public static class RequestStartCoachingSession {

        @Schema(description = "지도 학습 세션 ID", example = "1")
        private Long sessionId;

        @Schema(description = "코칭 옵션 타입", example = "WORD")
        private String optionType;

        @Builder
        public RequestStartCoachingSession(Long sessionId, String optionType) {
            this.sessionId = sessionId;
            this.optionType = optionType;
        }
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 세션 시작 응답 DTO")
    public static class ResponseStartCoachingSession {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "지도 학습 세션 ID", example = "1")
        private Long sessionId;

        @Schema(description = "AI 코칭 세션 상태", example = "RUNNING")
        private String coachingSessionStatus;

        @Schema(description = "사용자가 선택한 코칭 옵션", example = "WORD")
        private String selectedOption;

        @Schema(description = "현재 진행 중인 대화 턴 번호", example = "0")
        private Integer currentTurnOrder;
    }
}