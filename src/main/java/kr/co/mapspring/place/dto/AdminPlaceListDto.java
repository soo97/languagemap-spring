package kr.co.mapspring.place.dto;

import java.math.BigDecimal;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class AdminPlaceListDto {

    @Getter
    @Builder
    @Schema(name = "AdminPlaceListResponse", description = "장소 리스트 응답 DTO")
    public static class ResponseList {

        @Schema(
        		description = "장소 이름",
        		example = "스타벅스 강남점"
        		)
        private String placeName;

        @Schema(
        		description = "장소 주소",
        		example = "서울특별시 강남구 테헤란로 123"
        		)
        private String placeAddress;

        @Schema(
        		description = "구글 Place 고유 ID"
        		, example = "ChIJN1t_tDeuEmsRUsoyG83frY4"
        		)
        private String googlePlaceId;

        @Schema(
        		description = "위도",
        		example = "37.4979"
        		)
        private BigDecimal latitude;

        @Schema(
        		description = "경도",
        		example = "127.0276"
        		)
        private BigDecimal longitude;

        @Schema(
        		description = "지역 도시명",
        		example = "Seoul"
        		)
        private String regionCity;

        @Schema(
        		description = "연결된 시나리오 ID",
        		example = "1"
        		)
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