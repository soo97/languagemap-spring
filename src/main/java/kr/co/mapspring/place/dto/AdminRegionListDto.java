package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Region;
import lombok.Builder;
import lombok.Getter;

public class AdminRegionListDto {

    @Getter
    @Builder
    @Schema(name = "AdminRegionListResponse", description = "지역 리스트 응답 DTO")
    public static class ResponseList {

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
                description = "위도",
                example = "42.1555"
        		)
        private BigDecimal latitude;
        
        @Schema(
                description = "경도",
                example = "32.6667"
        		)
        private BigDecimal longitude;

        public static ResponseList from(Region region) {
            return ResponseList.builder()
                    .regionId(region.getRegionId())
                    .country(region.getCountry())
                    .city(region.getCity())
                    .latitude(region.getLatitude())
                    .longitude(region.getLongitude())
                    .build();
        }
    }
}