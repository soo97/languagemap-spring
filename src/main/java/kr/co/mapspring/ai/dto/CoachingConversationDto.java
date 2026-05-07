package kr.co.mapspring.ai.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
public class CoachingConversationDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "AI 코칭 스크립트 준비 요청 DTO")
    public static class RequestPrepareScript {

        private Long sessionId;
        private String optionType;

        private String placeName;
        private String country;
        private String city;
        private String placeAddress;
        private String evaluation;

        private List<FastApiOpenAiDto.PreviousMessage> previousMessages;

        @Builder
        public RequestPrepareScript(
                Long sessionId,
                String optionType,
                String placeName,
                String country,
                String city,
                String placeAddress,
                String evaluation,
                List<FastApiOpenAiDto.PreviousMessage> previousMessages
        ) {
            this.sessionId = sessionId;
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
    @Builder
    @Schema(description = "AI 코칭 스크립트 준비 응답 DTO")
    public static class ResponsePrepareScript {

        private Long coachingSessionId;
        private Long sessionId;
        private String coachingSessionStatus;
        private String selectedOption;
        private Integer currentTurnOrder;
        private List<CoachingScriptTurnDto.ResponseCoachingScriptTurn> turns;
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 대화 시작 응답 DTO")
    public static class ResponseStartConversation {

        private Long coachingSessionId;
        private Long coachingScriptTurnId;
        private Integer turnOrder;
        private String assistantText;
        private String assistantAudioUrl;
    }

    @Getter
    @Builder
    @Schema(description = "사용자 음성 처리 응답 DTO")
    public static class ResponseConversationTurn {

        private Long coachingSessionId;

        private Long userMessageId;
        private String recognizedText;
        private String userFeedback;
        private List<FastApiSpeechDto.PronunciationProblemWord> problemWords;

        private Double accuracyScore;
        private Double fluencyScore;
        private Double completenessScore;
        private Double pronunciationScore;

        private Boolean conversationEnded;

        private Long nextScriptTurnId;
        private Integer nextTurnOrder;
        private String nextAssistantText;
        private String nextAssistantAudioUrl;
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 최종 결과 응답 DTO")
    public static class ResponseFinishConversation {

        private CoachingFeedbackDto.ResponseCoachingFeedback feedback;

        private CoachingPronunciationResultDto.ResponseGetPronunciationResults pronunciationResults;

        private ContentDto.ResponseGetContents contents;
    }
}