package kr.co.mapspring.place.dto;

import lombok.Builder;
import lombok.Getter;

public class AdminCreateMissionDto {
	
	@Builder
	@Getter
	public static class RequestCreate {
		private String missionTitle;
		private String missionDescription;
		private Long scenarioId;
	}

}
