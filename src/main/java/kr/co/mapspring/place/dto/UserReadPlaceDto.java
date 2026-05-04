package kr.co.mapspring.place.dto;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class UserReadPlaceDto {

    @Builder
    @Getter
    @Schema(name = "UserReadPlaceResponse", description = "마커 상세 조회 응답 DTO")
    public static class ResponseRead {

        @Schema(
        		description = "장소 이름",
        		example = "광화문역"
        		)
        private String placeName;

        @Schema(
        		description = "장소 설명", 
        		example = "서울 중심부에 위치한 지하철역"
        		)
        private String placeDescription;

        @Schema(
        		description = "지역 도시",
        		example = "Seoul"
        		)
        private String regionCity;

        @Schema(
        		description = "지역 국가",
        		example = "Korea"
        		)
        private String regionCountry;

        @Schema(
        		description = "시나리오 카테고리", 
        		example = "TRANSPORT"
        		)
        private String scenarioCategory;

        @Schema(
        		description = "시나리오 설명",
        		example = "지하철역에서 길을 묻는 상황"
        		)
        private String scenarioDescription;

        @Schema(
        		description = "미션 리스트"
        		)
        private List<UserMissionListDto.ResponseList> mission;

        public static ResponseRead from(Place place, List<Mission> missionList) {
            return ResponseRead.builder()
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
                                    .toList()
                    )
                    .build();
        }
    }
}