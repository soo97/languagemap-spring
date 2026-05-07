package kr.co.mapspring.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.ai.entity.CoachingScriptTurn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CoachingScriptTurnDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "코칭 스크립트 턴 저장 요청 DTO")
    public static class RequestSaveCoachingScriptTurn {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "대화 진행 순서", example = "1")
        private Integer turnOrder;

        @Schema(description = "해당 턴에서 AI가 말할 문장", example = "Good morning. What would you like to order today?")
        private String assistantText;

        @Schema(description = "해당 턴에서 사용자가 말하기를 기대하는 기준 문장", example = "I would like a latte with almond milk, please.")
        private String expectedText;

        @Builder
        public RequestSaveCoachingScriptTurn(
                Long coachingSessionId,
                Integer turnOrder,
                String assistantText,
                String expectedText
        ) {
            this.coachingSessionId = coachingSessionId;
            this.turnOrder = turnOrder;
            this.assistantText = assistantText;
            this.expectedText = expectedText;
        }
    }

    @Getter
    @Builder
    @Schema(description = "코칭 스크립트 턴 응답 DTO")
    public static class ResponseCoachingScriptTurn {

        @Schema(description = "코칭 스크립트 턴 ID", example = "1")
        private Long coachingScriptTurnId;

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "대화 진행 순서", example = "1")
        private Integer turnOrder;

        @Schema(description = "AI 문장", example = "Good morning. What would you like to order today?")
        private String assistantText;

        @Schema(description = "사용자 기준 문장", example = "I would like a latte with almond milk, please.")
        private String expectedText;

        @Schema(description = "생성 시각", example = "2026-05-03T10:00:00")
        private LocalDateTime createdAt;

        public static ResponseCoachingScriptTurn from(CoachingScriptTurn turn) {
            return ResponseCoachingScriptTurn.builder()
                    .coachingScriptTurnId(turn.getCoachingScriptTurnId())
                    .coachingSessionId(turn.getCoachingSession().getCoachingSessionId())
                    .turnOrder(turn.getTurnOrder())
                    .assistantText(turn.getAssistantText())
                    .expectedText(turn.getExpectedText())
                    .createdAt(turn.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "코칭 스크립트 턴 목록 응답 DTO")
    public static class ResponseGetCoachingScriptTurns {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "코칭 스크립트 턴 목록")
        private List<ResponseCoachingScriptTurn> turns;
    }
}