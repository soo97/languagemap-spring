package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Region;
import lombok.Builder;
import lombok.Getter;

public class AdminReadRegionDto {
	
	 	@Getter
	    @Builder
	    @Schema(description = "지역 상세 응답 DTO")
	    public static class ResponseRead {
	        private Long regionId;
	        private String country;
	        private String city;
	        private BigDecimal latitude;
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
