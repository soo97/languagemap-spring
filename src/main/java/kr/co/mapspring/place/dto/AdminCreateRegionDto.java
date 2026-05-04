package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminCreateRegionDto {

    @Getter
    @Builder // 테스트 코드용
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "AdminCreateRegionRequest", description = "지역 생성 요청 DTO")
    public static class RequestCreate {

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
    }
}
