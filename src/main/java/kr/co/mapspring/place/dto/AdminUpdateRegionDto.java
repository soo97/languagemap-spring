package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminUpdateRegionDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "AdminUpdateRegionRequest", description = "지역 수정 요청 DTO")
    public static class RequestUpdate {

        @Schema(
                description = "국가명",
                example = "대한민국"
        		)
        private String country;

        @Schema(
                description = "도시명",
                example = "부산"
        		)
        private String city;

        @Schema(
                description = "지역 중심 위도",
                example = "35.1796"
        		)
        private BigDecimal latitude;

        @Schema(
                description = "지역 중심 경도",
                example = "129.0756"
        		)
        private BigDecimal longitude;
    }
}