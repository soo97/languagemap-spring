package kr.co.mapspring.place.dto;

import lombok.Builder;
import lombok.Getter;

public class AdminUpdateMissionDto {

	@Getter
	@Builder
	public static class RequestUpdate {
		private String missionTitle;
		private String missionDescription;
		private Long scenarioId;
	}


}
