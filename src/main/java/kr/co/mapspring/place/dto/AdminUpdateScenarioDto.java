package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.Builder;
import lombok.Getter;

public class AdminUpdateScenarioDto {
	
	@Builder
	@Getter
	public static class RequestUpdate {
		private String prompt;
		private String scenarioDescription;
		private ScenarioLevel level;
		private String category;
		private Integer completeExp;
	}

}
