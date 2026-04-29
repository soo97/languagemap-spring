package kr.co.mapspring.place.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class UserReadPlaceDto {
	
	@Builder
	@Getter
	@Schema(name = "UserReadPlaceResponse", description = "마커 상세 조회 DTO")
	public static class ResponseRead {
		private String placeName;
		private String placeDescription;
		private String regionCity;
		private String regionCountry;
		private String scenarioCategory;
		private String scenarioDescription;
		private List<UserMissionListDto.ResponseList> mission;
		
		public static UserReadPlaceDto.ResponseRead from(Place place, List<Mission> missionList) {
			
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
					.mission(
							missionList.stream()
							.map(UserMissionListDto.ResponseList::from)
							.collect(Collectors.toList()))
					.build();
		}
	}

}
