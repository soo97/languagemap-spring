package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;

public class AdminReadMissionDto {

	@Getter
	@Builder
	public static class ResponseRead {
		private String missionTitle;
		private String missionDescription;
		private MissionStatus missionStatus;
		private Long scenarioId;

		public static AdminReadMissionDto.ResponseRead from(Mission mission) {
			return AdminReadMissionDto.ResponseRead.builder()
					.missionTitle(mission.getMissionTitle())
					.missionDescription(mission.getMissionDescription())
					.missionStatus(mission.getMissionStatus())
					.scenarioId(mission.getScenario().getScenarioId())
					.build();
		}
	}
}
