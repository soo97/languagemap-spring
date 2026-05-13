package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.enums.LearningSessionStatus;
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
		
		@Schema(
				description = "지역 ID", 
				example = "1"
				)
		private Long regionId;
		
		@Schema(
				description = "학습 세션 Id", 
				example = "1"
				)
		private Long learningSessionId;
		
		@Schema(
				description = "학습 세션 상태", 
				example = "RUNNING"
				)
		private LearningSessionStatus learningStatus;
		
		 public static ResponseList from(
	                Place place,
	                LearningSession learningSession
	        ) {

	            return ResponseList.builder()
	                    .placeId(place.getPlaceId())
	                    .regionId(place.getRegion().getRegionId())
	                    .latitude(place.getLatitude())
	                    .longitude(place.getLongitude())

	                    .learningSessionId(
	                            learningSession != null
	                                    ? learningSession.getSessionId()
	                                    : null
	                    )

	                    .learningStatus(
	                            learningSession != null
	                                    ? learningSession.getStudyStatus()
	                                    : null
	                    )

	                    .build();
	        }
	}
}
