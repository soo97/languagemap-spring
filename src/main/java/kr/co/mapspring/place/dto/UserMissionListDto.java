package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import lombok.Builder;
import lombok.Getter;

public class UserMissionListDto {
	
	@Builder
	@Getter
	@Schema(name = "UserMissionListResponse", description = "미션 리스트 조회 DTO")
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
