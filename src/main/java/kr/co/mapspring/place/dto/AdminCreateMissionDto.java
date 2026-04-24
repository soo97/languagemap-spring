package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;

public class AdminCreateMissionDto {
	
	@Builder
	@Getter
	public static class RequestCreate {
		private String missionTitle;
		private String missionDescription;
		private MissionStatus missionStatus;
		private Long scenarioId;
	}

}
