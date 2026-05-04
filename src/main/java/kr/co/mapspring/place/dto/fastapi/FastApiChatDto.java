package kr.co.mapspring.place.dto.fastapi;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FastApiChatDto {
	
	@Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestChat {
        private String userMessage;
        private List<MessageHistory> messages;
        private String missionTitle;
        private String missionDescription;
        private String scenarioPrompt;
        private String scenarioCategory;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageHistory {
        private String role;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseChat {
        private String aiMessage;
    }


}
