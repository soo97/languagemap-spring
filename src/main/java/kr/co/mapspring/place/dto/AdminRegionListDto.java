package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Region;
import lombok.Builder;
import lombok.Getter;

public class AdminRegionListDto {
	
	 	@Getter
	    @Builder
	    @Schema(description = "지역 리스트 응답 DTO")
	    public static class ResponseList {
	        private Long regionId;
	        private String country;
	        private String city;

	        public static ResponseList from(Region region) {
	            return ResponseList.builder()
	                    .regionId(region.getRegionId())
	                    .country(region.getCountry())
	                    .city(region.getCity())
	                    .build();
	        }
	    }

}
