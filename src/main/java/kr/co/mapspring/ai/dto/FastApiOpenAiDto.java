package kr.co.mapspring.ai.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FastApiOpenAiDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 이전 대화 메시지 DTO")
    public static class PreviousMessage {

        @Schema(description = "메시지 역할", example = "USER")
        private CoachingMessageRole role;

        @Schema(description = "메시지 내용", example = "I would like a latte, please.")
        private String message;

        @Builder
        public PreviousMessage(CoachingMessageRole role, String message) {
            this.role = role;
            this.message = message;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 코칭 스크립트 생성 요청 DTO")
    public static class RequestCoachingScript {

        private String optionType;
        private String placeName;
        private String country;
        private String city;
        private String placeAddress;
        private String evaluation;
        private List<PreviousMessage> previousMessages;

        @Builder
        public RequestCoachingScript(
                String optionType,
                String placeName,
                String country,
                String city,
                String placeAddress,
                String evaluation,
                List<PreviousMessage> previousMessages
        ) {
            this.optionType = optionType;
            this.placeName = placeName;
            this.country = country;
            this.city = city;
            this.placeAddress = placeAddress;
            this.evaluation = evaluation;
            this.previousMessages = previousMessages;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 코칭 스크립트 생성 응답 DTO")
    public static class ResponseCoachingScript {

        private List<MessageItem> messages;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 메시지 항목 DTO")
    public static class MessageItem {

        private CoachingMessageRole role;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 최종 피드백 요청 DTO")
    public static class RequestFinalFeedback {

        private List<PreviousMessage> messages;
        private List<PronunciationResult> pronunciationResults;

        @Builder
        public RequestFinalFeedback(
                List<PreviousMessage> messages,
                List<PronunciationResult> pronunciationResults
        ) {
            this.messages = messages;
            this.pronunciationResults = pronunciationResults;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 발음 평가 결과 DTO")
    public static class PronunciationResult {

        private String expectedText;
        private String recognizedText;
        private Double accuracyScore;
        private Double fluencyScore;
        private Double completenessScore;

        @Builder
        public PronunciationResult(
                String expectedText,
                String recognizedText,
                Double accuracyScore,
                Double fluencyScore,
                Double completenessScore
        ) {
            this.expectedText = expectedText;
            this.recognizedText = recognizedText;
            this.accuracyScore = accuracyScore;
            this.fluencyScore = fluencyScore;
            this.completenessScore = completenessScore;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 최종 피드백 응답 DTO")
    public static class ResponseFinalFeedback {

        private Integer totalScore;
        private String summaryFeedback;
        private FeedbackSection naturalness;
        private FeedbackSection flow;
        private PronunciationSection pronunciation;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 피드백 섹션 DTO")
    public static class FeedbackSection {

        private String level;
        private String comment;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 발음 피드백 섹션 DTO")
    public static class PronunciationSection {

        private String level;
        private String comment;
        private List<ProblemWord> problemWords;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 문제 단어 DTO")
    public static class ProblemWord {

        private String word;
        private String audioUrl;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 추천 문장 요청 DTO")
    public static class RequestRecommendSentences {

        private String finalFeedback;

        @Builder
        public RequestRecommendSentences(String finalFeedback) {
            this.finalFeedback = finalFeedback;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 추천 문장 응답 DTO")
    public static class ResponseRecommendSentences {

        private List<String> sentences;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 유튜브 키워드 요청 DTO")
    public static class RequestYoutubeKeywords {

        private String finalFeedback;

        @Builder
        public RequestYoutubeKeywords(String finalFeedback) {
            this.finalFeedback = finalFeedback;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 유튜브 키워드 응답 DTO")
    public static class ResponseYoutubeKeywords {

        private List<String> keywords;
    }
}