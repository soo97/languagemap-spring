package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Scenario;
import lombok.Builder;
import lombok.Getter;

public class AdminReadScenarioDto {

    @Getter
    @Builder
    @Schema(name = "AdminReadScenarioResponse", description = "시나리오 상세 조회 응답 DTO")
    public static class ResponseRead {

        @Schema(
                description = "AI에게 전달되는 프롬프트",
                example = "You are a barista. Take a coffee order from the user."
        		)
        private String prompt;

        @Schema(
                description = "시나리오 설명",
                example = "카페에서 커피를 주문하는 상황"
        		)
        private String scenarioDescription;

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

        public static AdminReadScenarioDto.ResponseRead from(Scenario scenario) {
            return AdminReadScenarioDto.ResponseRead.builder()
                    .prompt(scenario.getPrompt())
                    .scenarioDescription(scenario.getScenarioDescription())
                    .category(scenario.getCategory())
                    .completeExp(scenario.getCompleteExp())
                    .build();
        }
    }
}