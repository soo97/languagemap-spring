package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.Builder;
import lombok.Getter;

public class AdminSaveScenarioDto {
	
	@Builder
	@Getter
	public static class RequestSave {
		private String prompt;
		private String scenarioDescription;
		private ScenarioLevel level;
		private String category;
		private Integer completeExp;
	}

}
