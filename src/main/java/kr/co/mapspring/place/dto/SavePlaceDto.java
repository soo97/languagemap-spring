package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import kr.co.mapspring.place.entity.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class SavePlaceDto {
	
	@Builder
	@Getter
	public static class RequsetSaveDto {
		private String googlePlaceId;
		private String placeName;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private String category;
		private String placeDescription;
	}
	
	@Builder
	@Getter
	public static class ResponseSaveDto {
		private Long placeId;
	}
	
	public static SavePlaceDto.ResponseSaveDto SavePlaceDtoFrom(Place savePlace) {
		return SavePlaceDto.ResponseSaveDto.builder()
  			  .placeId(savePlace.getPlaceId())
  			  .build();
		
	}

}
