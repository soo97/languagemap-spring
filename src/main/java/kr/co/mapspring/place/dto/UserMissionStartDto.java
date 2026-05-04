package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.enums.MissionSessionStatus;
import lombok.Builder;
import lombok.Getter;

public class UserMissionStartDto {
	
	@Builder
	@Getter
	@Schema(name = "UserMissionStartResponse", description = "미션 시작 응답 DTO")
	public static class ResponseMissionStart {
		
		@Schema(
				description = "미션 ID", 
				example = "1"
				)
		private Long missionId;
		
		@Schema(
				description = "미션 진행 상태",
				example = "RUNNING"
				)
		private MissionSessionStatus missionStatus;
		
		@Schema(
				description = "미션 제목",
				example = "출구 묻기"
				)
		private String missionTitle;
		
		@Schema(
				description = "미션 설명",
				example = "원하는 출구를 영어로 물어보세요."
				)
		private String missionDescription;
		
		@Schema(
				description = "AI 첫 메시지",
				example = "Hello! Which exit are you looking for?"
				)
	    private String aiMessage;
		
		public static UserMissionStartDto.ResponseMissionStart of(Mission mission, 
														   MissionSession missionSession,
														   String aiMessage) {
			return UserMissionStartDto.ResponseMissionStart.builder()
					.missionId(mission.getMissionId())
					.missionStatus(missionSession.getMissionStatus())
					.missionTitle(mission.getMissionTitle())
					.missionDescription(mission.getMissionDescription())
					.aiMessage(aiMessage)
					.build();
		}
	}

}
