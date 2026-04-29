package kr.co.mapspring.place.dto;

import kr.co.mapspring.place.enums.MissionStatus;

public class UserReadMissionDto {
	
	public static class ResponseRead {
		
		private String missionTitle;
		private String missionDescription;
		private MissionStatus missionStatusl;
	}

}
