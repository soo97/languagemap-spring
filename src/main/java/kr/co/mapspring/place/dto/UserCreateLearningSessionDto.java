package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.enums.LearningSessionLevel;
import kr.co.mapspring.place.enums.LearningSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserCreateLearningSessionDto {
	
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(name = "UserCreateLearningSessionRequest", description = "학습 세선 생성 DTO")
	public static class RequestCreate {
		private Long userId;
		private LearningSessionLevel level;
	}
	
	@Getter
	@Builder
	@Schema(name = "AdminCreateLearningSessionResponse", description = "학습 세선 응답 DTO")
	public static class ResponseCreate {
		private Long learningSessionId;
		private LearningSessionStatus learningSessionStatus;
		
		public static UserCreateLearningSessionDto.ResponseCreate from(LearningSession learningSession) {
			return UserCreateLearningSessionDto.ResponseCreate.builder()
					.learningSessionId(learningSession.getSessionId())
					.learningSessionStatus(learningSession.getStudyStatus())
					.build();
		}
	}
	

}
