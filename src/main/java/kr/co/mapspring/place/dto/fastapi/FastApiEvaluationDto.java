package kr.co.mapspring.place.dto.fastapi;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FastApiEvaluationDto {
	
	@Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestEvaluation {
        private String scenarioPrompt;
        private String scenarioCategory;
        private List<MessageHistory> messages;
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
    public static class ResponseEvaluation {
        private String evaluation;
    }

}
