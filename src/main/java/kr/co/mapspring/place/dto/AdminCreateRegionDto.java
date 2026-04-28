package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminCreateRegionDto {

	@Getter
    @NoArgsConstructor
    @Schema(description = "지역 생성 요청 DTO")
    public static class RequestCreate {
        private String country;
        private String city;
        private BigDecimal latitude;
        private BigDecimal longitude;
    }
}
