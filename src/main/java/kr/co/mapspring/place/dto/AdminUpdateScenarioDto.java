package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.Builder;
import lombok.Getter;

public class AdminUpdateScenarioDto {

    @Getter
    @Builder
    @Schema(name = "AdminUpdateScenarioRequest", description = "시나리오 수정 요청 DTO")
    public static class RequestUpdate {

        @Schema(
                description = "수정할 프롬프트",
                example = "You are a barista. Ask the customer what they want to drink."
        )
        private String prompt;

        @Schema(
                description = "수정할 시나리오 설명",
                example = "카페에서 커피를 주문하는 상황"
        )
        private String scenarioDescription;

        @Schema(
                description = "수정할 난이도 (BEGINNER, INTERMEDIATE, ADVANCED)",
                example = "INTERMEDIATE"
        )
        private ScenarioLevel level;

        @Schema(
                description = "수정할 카테고리",
                example = "CAFE"
        )
        private String category;

        @Schema(
                description = "수정할 완료 경험치",
                example = "20"
        )
        private Integer completeExp;
    }
}