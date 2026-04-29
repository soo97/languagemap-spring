package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminCreateScenarioDto {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
    @Schema(name = "AdminCreateScenarioRequest", description = "시나리오 생성 요청 DTO")
    public static class RequestCreate {

        @Schema(
                description = "AI에게 전달할 프롬프트",
                example = "You are a barista. Take a coffee order from the user."
        )
        private String prompt;

        @Schema(
                description = "시나리오 설명",
                example = "카페에서 커피를 주문하는 상황"
        )
        private String scenarioDescription;

        @Schema(
                description = "시나리오 난이도 (BEGINNER, INTERMEDIATE, ADVANCED)",
                example = "BEGINNER"
        )
        private ScenarioLevel level;

        @Schema(
                description = "시나리오 카테고리",
                example = "CAFE"
        )
        private String category;

        @Schema(
                description = "완료 시 획득 경험치",
                example = "10"
        )
        private Integer completeExp;
    }
}