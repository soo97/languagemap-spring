package kr.co.mapspring.place.dto;

import lombok.Builder;
import lombok.Getter;

public class AdminUpdatePlaceDto {
	
	@Builder
	@Getter
	public static class RequestUpdate {
		private String placeName;
		private String placeDescription;
		private Long scenarioId;
	}

}
