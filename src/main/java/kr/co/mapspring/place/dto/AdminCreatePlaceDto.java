package kr.co.mapspring.place.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;


public class AdminCreatePlaceDto {
	
	@Builder
	@Getter
	public static class RequestCreate {
		private String googlePlaceId;
		private String placeName;
		private String placeAddress;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String placeDescription;
		private Long scenarioId;
		private Long regionId;
	}

}
