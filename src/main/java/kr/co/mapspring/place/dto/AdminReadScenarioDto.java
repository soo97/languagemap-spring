package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.Builder;
import lombok.Getter;

public class AdminReadScenarioDto {
	
	@Getter
	@Builder
	public static class RequestRead {
		private Long scenarioId;
	}
	
	@Getter
	@Builder
	public static class ResponseRead {
		private String prompt;
		private String scenarioDescription;
		private ScenarioLevel level;
		private String category;
		private Integer completeExp;
		
		public static AdminReadScenarioDto.ResponseRead from (Scenario scenario) {
			
			return AdminReadScenarioDto.ResponseRead.builder()
					.prompt(scenario.getPrompt())
					.scenarioDescription(scenario.getScenarioDescription())
					.level(scenario.getLevel())
					.category(scenario.getCategory())
					.completeExp(scenario.getCompleteExp())
					.build();
		}
	}

}
