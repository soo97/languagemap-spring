package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.enums.LearningSessionStatus;
import kr.co.mapspring.place.enums.MissionSessionStatus;
import lombok.Builder;
import lombok.Getter;

public class UserMissionCompleteDto {
	
	@Getter
	@Builder
	@Schema(name = "UserMissionCompleteResponse", description = "미션 완료 응답 DTO")
	public static class ResponseComplete {

	    @Schema(description = "미션 진행 상태", example = "COMPLETED")
	    private MissionSessionStatus missionStatus;

	    @Schema(description = "학습 세션 상태", example = "RUNNING")
	    private LearningSessionStatus learningSessionStatus;
	    
	    @Schema(
                description = "학습 세션 최종 평가. 모든 미션 완료 시에만 값이 반환된다.",
                example = "전체적으로 자연스럽게 대화했습니다. 목적지를 먼저 말한 점이 좋았습니다."
        )
        private String evaluation;

	    public static ResponseComplete of(
	            MissionSession missionSession,
	            LearningSession learningSession,
	            String evaluation
	    ) {
	        return ResponseComplete.builder()
	                .missionStatus(missionSession.getMissionStatus())
	                .learningSessionStatus(learningSession.getStudyStatus())
	                .evaluation(evaluation)
	                .build();
	    }
	}

}
