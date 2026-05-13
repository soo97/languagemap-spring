package kr.co.mapspring.place.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserRecentLearningPlaceDto {
	
	@Getter
	@Builder
	@AllArgsConstructor
	@Schema(name = "UserRecentLearningPlaceResponse", description = "사용자 최근 학습 장소 리스트")
	public static class ResponseRecent {
		
		@Schema(
                description = "장소 ID",
                example = "1"
        		)
		private Long placeId;
		
		@Schema(
                description = "장소 ID",
                example = "스타벅스 부평점"
        		)
	    private String placeName;
	    
		@Schema(
                description = "시나리오 설명",
                example = "카페에서 주문하는 상황"
        		)
	    private String scenarioDescription;
	    
		@Schema(
                description = "종료 시간",
                example = "2026-05-12 12:08"
        		)
	    private LocalDateTime endTime;
	    
		public static UserRecentLearningPlaceDto.ResponseRecent of(LearningSession learningSession) {
	        Place place = learningSession.getPlace();

	        return UserRecentLearningPlaceDto.ResponseRecent.builder()
	                .placeId(place.getPlaceId()) 
	                .placeName(place.getPlaceName())
	                .scenarioDescription(place.getScenario().getScenarioDescription())
	                .endTime(learningSession.getEndTime())
	                .build();

		}
	}
}
