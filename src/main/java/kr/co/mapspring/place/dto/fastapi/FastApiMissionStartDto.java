package kr.co.mapspring.place.dto.fastapi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FastApiMissionStartDto {

	@Builder
	@Getter
	public static class RequestMissionStart {
		
		 private String level;
		 private String scenarioPrompt;
		 private String scenarioCategory;
		 private String missionTitle;
		 private String missionDescription;
	}
	
	@Getter
	@NoArgsConstructor
	public static class ResponseMissionStart {
		
		private String aiMessage;
	}

}
