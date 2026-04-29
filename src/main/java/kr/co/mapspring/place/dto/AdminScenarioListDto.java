package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.Builder;
import lombok.Getter;

public class AdminScenarioListDto {

    @Getter
    @Builder
    @Schema(description = "시나리오 리스트 응답 DTO")
    public static class ResponseList {

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

        public static AdminScenarioListDto.ResponseList from(Scenario scenario) {
            return AdminScenarioListDto.ResponseList.builder()
                    .scenarioDescription(scenario.getScenarioDescription())
                    .level(scenario.getLevel())
                    .category(scenario.getCategory())
                    .build();
        }
    }
}