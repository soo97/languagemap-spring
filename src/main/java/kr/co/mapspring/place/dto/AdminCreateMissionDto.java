package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminCreateMissionDto {
	
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(name = "AdminCreateMissionRequest", description = "미션 생성 요청 DTO")
	public static class RequestCreate {
		@Schema(
				description = "미션 제목", 
				example = "카페 주문 미션"
				)
		private String missionTitle;
		@Schema(
				description = "미션 설명",
				example = "커피를 주문하는 상황을 연습한다."
				)
		private String missionDescription;
		@Schema(
				description = "연결된 시나리오 ID",
				example = "1"
				)
		private Long scenarioId;
	}

}
