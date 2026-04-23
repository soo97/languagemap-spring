package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class AdminReadPlaceDto {
	
	@Builder
	@Getter
	public static class RequestRead {
		private Long placeId;
	}
	
	@Builder
	@Getter
	public static class ResponseRead {
		private String placeName;
		private String placeDescription;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String scenarioDescription;
		private String city;
		
	}
	
	public static AdminReadPlaceDto.ResponseRead from (Place place) {
		return AdminReadPlaceDto.ResponseRead.builder()
				.placeName(place.getPlaceName())
				.placeDescription(place.getPlaceDescription())
				.latitude(place.getLatitude())
				.longitude(place.getLongitude())
				.city(place.getRegion().getCity())
				.scenarioDescription(place.getScenario().getScenarioDescription())
				.build();
	}

}
