package kr.co.mapspring.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.ai.entity.CoachingPronunciationResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CoachingPronunciationResultDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "발음 평가 결과 저장 요청 DTO")
    public static class RequestSavePronunciationResult {

        @Schema(description = "평가 대상 USER 메시지 ID", example = "1")
        private Long coachingMessageId;

        @Schema(description = "기준 문장이 저장된 스크립트 턴 ID", example = "1")
        private Long coachingScriptTurnId;

        @Schema(description = "음성 인식 결과 문장", example = "I would like a latte with almond milk please.")
        private String recognizedText;

        @Schema(description = "발음 정확도 점수", example = "96.0")
        private Double accuracyScore;

        @Schema(description = "발화 유창성 점수", example = "81.0")
        private Double fluencyScore;

        @Schema(description = "문장 완성도 점수", example = "100.0")
        private Double completenessScore;

        @Schema(description = "종합 발음 점수", example = "87.8")
        private Double pronunciationScore;

        @Schema(description = "해당 문장에 대한 짧은 발음 피드백", example = "almond 발음을 조금 더 명확하게 연습하면 좋습니다.")
        private String feedback;

        @Schema(description = "문제가 된 단어 목록 JSON", example = "[\"almond\", \"preferably\"]")
        private String problemWords;

        @Builder
        public RequestSavePronunciationResult(
                Long coachingMessageId,
                Long coachingScriptTurnId,
                String recognizedText,
                Double accuracyScore,
                Double fluencyScore,
                Double completenessScore,
                Double pronunciationScore,
                String feedback,
                String problemWords
        ) {
            this.coachingMessageId = coachingMessageId;
            this.coachingScriptTurnId = coachingScriptTurnId;
            this.recognizedText = recognizedText;
            this.accuracyScore = accuracyScore;
            this.fluencyScore = fluencyScore;
            this.completenessScore = completenessScore;
            this.pronunciationScore = pronunciationScore;
            this.feedback = feedback;
            this.problemWords = problemWords;
        }
    }

    @Getter
    @Builder
    @Schema(description = "발음 평가 결과 응답 DTO")
    public static class ResponsePronunciationResult {

        @Schema(description = "발음 평가 결과 ID", example = "1")
        private Long pronunciationResultId;

        @Schema(description = "평가 대상 USER 메시지 ID", example = "1")
        private Long coachingMessageId;

        @Schema(description = "기준 문장이 저장된 스크립트 턴 ID", example = "1")
        private Long coachingScriptTurnId;

        @Schema(description = "사용자가 말해야 하는 기준 문장", example = "I would like a latte with almond milk, please.")
        private String expectedText;

        @Schema(description = "음성 인식 결과 문장", example = "I would like a latte with almond milk please.")
        private String recognizedText;

        @Schema(description = "발음 정확도 점수", example = "96.0")
        private Double accuracyScore;

        @Schema(description = "발화 유창성 점수", example = "81.0")
        private Double fluencyScore;

        @Schema(description = "문장 완성도 점수", example = "100.0")
        private Double completenessScore;

        @Schema(description = "종합 발음 점수", example = "87.8")
        private Double pronunciationScore;

        @Schema(description = "해당 문장에 대한 짧은 발음 피드백", example = "almond 발음을 조금 더 명확하게 연습하면 좋습니다.")
        private String feedback;

        @Schema(description = "문제가 된 단어 목록 JSON", example = "[\"almond\", \"preferably\"]")
        private String problemWords;

        @Schema(description = "생성 시각", example = "2026-05-03T10:00:00")
        private LocalDateTime createdAt;

        public static ResponsePronunciationResult from(CoachingPronunciationResult result) {
            return ResponsePronunciationResult.builder()
                    .pronunciationResultId(result.getPronunciationResultId())
                    .coachingMessageId(result.getCoachingMessage().getCoachingMessageId())
                    .coachingScriptTurnId(result.getCoachingScriptTurn().getCoachingScriptTurnId())
                    .expectedText(result.getCoachingScriptTurn().getExpectedText())
                    .recognizedText(result.getRecognizedText())
                    .accuracyScore(result.getAccuracyScore())
                    .fluencyScore(result.getFluencyScore())
                    .completenessScore(result.getCompletenessScore())
                    .pronunciationScore(result.getPronunciationScore())
                    .feedback(result.getFeedback())
                    .problemWords(result.getProblemWords())
                    .createdAt(result.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "발음 평가 결과 목록 응답 DTO")
    public static class ResponseGetPronunciationResults {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "발음 평가 결과 목록")
        private List<ResponsePronunciationResult> pronunciationResults;
    }
}