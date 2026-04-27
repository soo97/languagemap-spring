package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.Builder;
import lombok.Getter;

public class AdminScenarioListDto {
	
	@Builder
	@Getter
	public static class ResponseList {
		
		private String scenarioDescription;
		private ScenarioLevel level;
		private String category;
		
		public static AdminScenarioListDto.ResponseList from(Scenario scenario) {
			return AdminScenarioListDto.ResponseList.builder()
					.scenarioDescription(scenario.getScenarioDescription())
					.level(scenario.getLevel())
					.category(scenario.getCategory())
					.build();
		}
	}

}
