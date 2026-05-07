package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

public class UserPlaceListDto {

	@Getter
	@Builder
	 @Schema(name = "UserPlaceListResponse", description = "마커 조회용 DTO")
	public static class ResponseList {
		@Schema(
				description = "장소 ID", 
				example = "1"
				)
		private Long placeId;
		@Schema(
				description = "위도", 
				example = "38.1155"
				)
		private BigDecimal latitude;
		@Schema(
				description = "경도", 
				example = "42.8422"
				)
		private BigDecimal longitude;
		
		public static UserPlaceListDto.ResponseList from(Place place) {
            return UserPlaceListDto.ResponseList.builder()
                    .placeId(place.getPlaceId())
                    .latitude(place.getLatitude())
                    .longitude(place.getLongitude())
                    .build();
        }
	}
}
