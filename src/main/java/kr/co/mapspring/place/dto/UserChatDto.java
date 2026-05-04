package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserChatDto {
	
	@Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "UserChatRequest", description = "AI 채팅 요청 DTO")
    public static class RequestChat {

        @Schema(
                description = "학습 세션 ID",
                example = "1"
        		)
        private Long sessionId;

        @Schema(
                description = "사용자 메시지",
                example = "How can I get to exit 3?"
        		)
        private String message;
    }

    @Getter
    @Builder
    @Schema(name = "UserChatResponse", description = "AI 채팅 응답 DTO")
    public static class ResponseChat {

        @Schema(
                description = "AI 응답 메시지",
                example = "You can say: Could you tell me how to get to exit 3?"
        		)
        private String aiMessage;
    }	
}
