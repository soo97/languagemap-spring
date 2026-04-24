package kr.co.mapspring.ai.dto;

import lombok.Builder;
import lombok.Getter;

public class StartCoachingSessionDto {
	
	 	@Getter
	    @Builder
	    public static class RequestStartCoachingSession {
	        private Long sessionId;
	        private String optionType;
	    }

	    @Getter
	    @Builder
	    public static class ResponseStartCoachingSession {
	        private Long coachingSessionId;
	        private Long sessionId;
	        private String coachingSessionStatus;
	    }
}
