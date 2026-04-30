package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Region;
import lombok.Builder;
import lombok.Getter;

public class AdminReadRegionDto {

    @Getter
    @Builder
    @Schema(name = "AdminReadRegionResponse", description = "지역 상세 응답 DTO")
    public static class ResponseRead {

        @Schema(
                description = "지역 ID",
                example = "10"
        		)
        private Long regionId;

        @Schema(
                description = "국가명",
                example = "대한민국"
        		)
        private String country;

        @Schema(
                description = "도시명",
                example = "서울"
        		)
        private String city;

        @Schema(
                description = "지역 중심 위도",
                example = "37.5665"
        		)
        private BigDecimal latitude;

        @Schema(
                description = "지역 중심 경도",
                example = "126.9780"
        		)
        private BigDecimal longitude;

        public static ResponseRead from(Region region) {
            return ResponseRead.builder()
                    .regionId(region.getRegionId())
                    .country(region.getCountry())
                    .city(region.getCity())
                    .latitude(region.getLatitude())
                    .longitude(region.getLongitude())
                    .build();
        }
    }
}