package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.entity.Mission;
import lombok.Builder;
import lombok.Getter;

public class AdminMissionListDto {

	@Getter
	@Builder
	public static class ResponseList {
		private String missionTitle;
		private String missionDescription;

		public static AdminMissionListDto.ResponseList from(Mission mission) {
			return ResponseList.builder()
					.missionTitle(mission.getMissionTitle())
					.missionDescription(mission.getMissionDescription())
					.build();
		}
	}
}
