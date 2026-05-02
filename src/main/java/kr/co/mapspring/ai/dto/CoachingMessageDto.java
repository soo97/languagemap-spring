package kr.co.mapspring.ai.dto;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

public class CoachingMessageDto {

	@Getter
    @NoArgsConstructor
    @Schema(description = "AI 코칭 메시지 저장 요청 DTO")
    public static class RequestSaveCoachingMessage {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "메시지 역할", example = "USER")
        private CoachingMessageRole role;

        @Schema(description = "메시지 내용", example = "Could you make it less sweet?")
        private String message;
        
        @Schema(description = "메시지와 연결된 음성 파일 URL", example = "/static/audio/user-1.wav")
        private String audioUrl;

        @Builder
        public RequestSaveCoachingMessage(
                Long coachingSessionId,
                CoachingMessageRole role,
                String message,
                String audioUrl
        ) {
            this.coachingSessionId = coachingSessionId;
            this.role = role;
            this.message = message;
            this.audioUrl = audioUrl;
        }
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 메시지 응답 DTO")
    public static class ResponseCoachingMessage {

        @Schema(description = "AI 코칭 메시지 ID", example = "1")
        private Long coachingMessageId;

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "메시지 역할", example = "USER")
        private CoachingMessageRole role;

        @Schema(description = "메시지 내용", example = "Could you make it less sweet?")
        private String message;
        
        @Schema(description = "메시지와 연결된 음성 파일 URL", example = "/static/audio/user-1.wav")
        private String audioUrl;

        @Schema(description = "메시지 생성 시각", example = "2026-04-26T17:40:00")
        private LocalDateTime createdAt;

        public static ResponseCoachingMessage from(CoachingMessage coachingMessage) {
            return ResponseCoachingMessage.builder()
                    .coachingMessageId(coachingMessage.getCoachingMessageId())
                    .coachingSessionId(coachingMessage.getCoachingSession().getCoachingSessionId())
                    .role(coachingMessage.getRole())
                    .message(coachingMessage.getMessage())
                    .audioUrl(coachingMessage.getAudioUrl())
                    .createdAt(coachingMessage.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "AI 코칭 메시지 목록 응답 DTO")
    public static class ResponseGetCoachingMessages {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "AI 코칭 메시지 목록")
        private List<ResponseCoachingMessage> messages;
    }
}