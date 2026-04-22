package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class ReadPlaceDto {
	
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
		private BigDecimal latutude;
		private BigDecimal longitude;
		private String scenarioDescription;
		private String City;
		
	}
	
	public static ReadPlaceDto.ResponseRead from (Place place) {
		return ReadPlaceDto.ResponseRead.builder()
				.placeName(place.getPlaceName())
				.placeDescription(place.getPlaceDescription())
				.latutude(place.getLatitude())
				.longitude(place.getLongitude())
				.City(place.getRegion().getCity())
				.scenarioDescription(place.getScenario().getScenariosDescription())
				.build();
	}

}
