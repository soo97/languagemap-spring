package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import lombok.Builder;
import lombok.Getter;


public class SavePlaceDto {
	
	@Builder
	@Getter
	public static class RequsetSaveDto {
		private String googlePlaceId;
		private String placeName;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String placeDescription;
		private Long scenario;
		private Long region;
	}
	
	@Builder
	@Getter
	public static class ResponseSaveDto {
		private Long placeId;
		private String googlePlaceId;
		private String placeName;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String placeDescription;
		private Scenario scenario;
		private Region region;
	}

}
