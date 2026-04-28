package kr.co.mapspring.place.dto;

import java.math.BigDecimal;
import java.util.Optional;

import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class AdminPlaceListDto {
	
	@Builder
	@Getter
	public static class ResponseList {
		private String placeName;
		private String placeAddress;
		private String googlePlaceId;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String regionCity;
		private Long scenarioId;
		
		public static AdminPlaceListDto.ResponseList from(Place place) {
			return AdminPlaceListDto.ResponseList.builder()
					.placeName(place.getPlaceName())
					.placeAddress(place.getPlaceAddress())
					.googlePlaceId(place.getGooglePlaceId())
					.latitude(place.getLatitude())
					.longitude(place.getLongitude())
					.regionCity(Optional.ofNullable(place.getRegion())
							.map(region -> region.getCity())
							.orElse(null))
					.scenarioId(Optional.ofNullable(place.getScenario())
							.map(scenario -> scenario.getScenarioId())
							.orElse(null))
					.build();
		}
	}

}
