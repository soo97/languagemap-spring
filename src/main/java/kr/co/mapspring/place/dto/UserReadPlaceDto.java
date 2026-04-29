package kr.co.mapspring.place.dto;

import java.util.Optional;

import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class UserReadPlaceDto {
	
	@Builder
	@Getter
	public static class ResponseRead {
		private String placeName;
		private String placeDescription;
		private String regionCity;
		private String regionCountry;
		private String scenarioCategory;
		private String scenarioDescription;
		
		public static UserReadPlaceDto.ResponseRead from(Place place) {
			return UserReadPlaceDto.ResponseRead.builder()
					.placeName(place.getPlaceName())
					.placeDescription(place.getPlaceDescription())
					.regionCity(Optional.ofNullable(place.getRegion())
							.map(region -> region.getCity())
							.orElse(null))
					.regionCountry(Optional.ofNullable(place.getRegion())
							.map(region -> region.getCountry())
							.orElse(null))
					.scenarioCategory(Optional.ofNullable(place.getScenario())
							.map(scenario -> scenario.getCategory())
							.orElse(null))
					.scenarioDescription(Optional.ofNullable(place.getScenario())
							.map(scenario -> scenario.getScenarioDescription())
							.orElse(null))
					.build();
		}
	}

}
