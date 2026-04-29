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
    @Schema(description = "지역 수정 요청 DTO")
    public static class RequestUpdate {
        private String country;
        private String city;
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

}
