package kr.co.mapspring.place.dto;

import java.math.BigDecimal;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class AdminReadPlaceDto {

    @Getter
    @Builder
    @Schema(description = "장소 상세 조회 응답 DTO")
    public static class ResponseRead {

        @Schema(
                description = "장소 이름",
                example = "스타벅스 강남점"
        )
        private String placeName;

        @Schema(
                description = "장소 설명",
                example = "조용한 분위기의 카페"
        )
        private String placeDescription;

        @Schema(
                description = "장소 주소",
                example = "서울특별시 강남구 테헤란로 123"
        )
        private String placeAddress;

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
                description = "연결된 시나리오 설명",
                example = "카페에서 커피를 주문하는 상황"
        )
        private String scenarioDescription;

        @Schema(
                description = "지역 도시명",
                example = "Seoul"
        )
        private String city;

        public static AdminReadPlaceDto.ResponseRead from(Place place) {
            return AdminReadPlaceDto.ResponseRead.builder()
                    .placeName(place.getPlaceName())
                    .placeDescription(place.getPlaceDescription())
                    .latitude(place.getLatitude())
                    .longitude(place.getLongitude())
                    .placeAddress(place.getPlaceAddress())
                    .city(Optional.ofNullable(place.getRegion())
                            .map(region -> region.getCity())
                            .orElse(null))
                    .scenarioDescription(Optional.ofNullable(place.getScenario())
                            .map(scenario -> scenario.getScenarioDescription())
                            .orElse(null))
                    .build();
        }
    }
}