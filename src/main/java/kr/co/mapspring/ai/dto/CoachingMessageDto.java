package kr.co.mapspring.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import lombok.Builder;
import lombok.Getter;

public class CoachingMessageDto {

    @Getter
    @Builder
    public static class RequestSaveCoachingMessage {
        private Long coachingSessionId;
        private CoachingMessageRole role;
        private String message;
    }

    @Getter
    @Builder
    public static class ResponseCoachingMessage {
        private Long coachingMessageId;
        private Long coachingSessionId;
        private CoachingMessageRole role;
        private String message;
        private LocalDateTime createdAt;

        public static ResponseCoachingMessage from(CoachingMessage coachingMessage) {
            return ResponseCoachingMessage.builder()
                    .coachingMessageId(coachingMessage.getCoachingMessageId())
                    .coachingSessionId(coachingMessage.getCoachingSession().getCoachingSessionId())
                    .role(coachingMessage.getRole())
                    .message(coachingMessage.getMessage())
                    .createdAt(coachingMessage.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseGetCoachingMessages {
        private Long coachingSessionId;
        private List<ResponseCoachingMessage> messages;
    }
}