package kr.co.mapspring.ai.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.ai.entity.CoachingFeedback;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CoachingFeedbackDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "AI 코칭 최종 피드백 저장 요청 DTO")
    public static class RequestSaveCoachingFeedback {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "최종 종합 말하기 점수", example = "87")
        private Integer totalScore;

        @Schema(description = "AI가 생성한 최종 종합 피드백", example = "전체적으로 자연스럽게 답변했지만 일부 발음 개선이 필요합니다.")
        private String summaryFeedback;

        @Schema(description = "자연스러움 평가 등급", example = "GOOD")
        private String naturalnessLevel;

        @Schema(description = "자연스러움 평가 설명", example = "문장이 자연스럽게 이어졌습니다.")
        private String naturalnessComment;

        @Schema(description = "응답 흐름 평가 등급", example = "GOOD")
        private String flowLevel;

        @Schema(description = "응답 흐름 평가 설명", example = "질문에 맞게 적절히 답변했습니다.")
        private String flowComment;

        @Schema(description = "발음 평가 등급", example = "CHECK")
        private String pronunciationLevel;

        @Schema(description = "발음 평가 설명", example = "almond, preferably 발음을 더 연습하면 좋습니다.")
        private String pronunciationComment;

        @Schema(description = "최종 피드백 기준 문제 단어 목록 JSON", example = "[\"almond\", \"preferably\"]")
        private String problemWords;

        @Builder
        public RequestSaveCoachingFeedback(
                Long coachingSessionId,
                Integer totalScore,
                String summaryFeedback,
                String naturalnessLevel,
                String naturalnessComment,
                String flowLevel,
                String flowComment,
                String pronunciationLevel,
                String pronunciationComment,
                String problemWords
        ) {
            this.coachingSessionId = coachingSessionId;
            this.totalScore = totalScore;
            this.summaryFeedback = summaryFeedback;
            this.naturalnessLevel = naturalnessLevel;
            this.naturalnessComment = naturalnessComment;
            this.flowLevel = flowLevel;
            this.flowComment = flowComment;
            this.pronunciationLevel = pronunciationLevel;
            this.pronunciationComment = pronunciationComment;
            this.problemWords = problemWords;
        }
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 최종 피드백 응답 DTO")
    public static class ResponseCoachingFeedback {

        @Schema(description = "AI 코칭 최종 피드백 ID", example = "1")
        private Long coachingFeedbackId;

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "최종 종합 말하기 점수", example = "87")
        private Integer totalScore;

        @Schema(description = "AI가 생성한 최종 종합 피드백")
        private String summaryFeedback;

        @Schema(description = "자연스러움 평가 등급", example = "GOOD")
        private String naturalnessLevel;

        @Schema(description = "자연스러움 평가 설명")
        private String naturalnessComment;

        @Schema(description = "응답 흐름 평가 등급", example = "GOOD")
        private String flowLevel;

        @Schema(description = "응답 흐름 평가 설명")
        private String flowComment;

        @Schema(description = "발음 평가 등급", example = "CHECK")
        private String pronunciationLevel;

        @Schema(description = "발음 평가 설명")
        private String pronunciationComment;

        @Schema(description = "최종 피드백 기준 문제 단어 목록 JSON")
        private String problemWords;

        @Schema(description = "생성 시각", example = "2026-05-03T10:00:00")
        private LocalDateTime createdAt;

        public static ResponseCoachingFeedback from(CoachingFeedback feedback) {
            return ResponseCoachingFeedback.builder()
                    .coachingFeedbackId(feedback.getCoachingFeedbackId())
                    .coachingSessionId(feedback.getCoachingSession().getCoachingSessionId())
                    .totalScore(feedback.getTotalScore())
                    .summaryFeedback(feedback.getSummaryFeedback())
                    .naturalnessLevel(feedback.getNaturalnessLevel())
                    .naturalnessComment(feedback.getNaturalnessComment())
                    .flowLevel(feedback.getFlowLevel())
                    .flowComment(feedback.getFlowComment())
                    .pronunciationLevel(feedback.getPronunciationLevel())
                    .pronunciationComment(feedback.getPronunciationComment())
                    .problemWords(feedback.getProblemWords())
                    .createdAt(feedback.getCreatedAt())
                    .build();
        }
    }
}