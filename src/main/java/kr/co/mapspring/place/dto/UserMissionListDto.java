package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.entity.Mission;
import lombok.Builder;
import lombok.Getter;

public class UserMissionListDto {
	
	@Builder
	@Getter
	public static class ResponseList {
		
		private String missionTitle;
		private String missionDescription;
		
		public static UserMissionListDto.ResponseList from (Mission mission) {
			return UserMissionListDto.ResponseList.builder()
					.missionTitle(mission.getMissionTitle())
					.missionDescription(mission.getMissionDescription())
					.build();
		}
	}

}
