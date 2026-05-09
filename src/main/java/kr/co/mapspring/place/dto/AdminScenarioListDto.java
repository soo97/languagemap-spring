package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Scenario;
import lombok.Builder;
import lombok.Getter;

public class AdminScenarioListDto {

    @Getter
    @Builder
    @Schema(name = "AdminScenarioListResponse", description = "시나리오 리스트 응답 DTO")
    public static class ResponseList {
    	
    	@Schema(
                description = "시나리오 ID",
                example = "1"
        		)
    	private Long scenarioId;

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
                description = "시나리오 경험치",
                example = "120"
        		)
        private Integer completeExp;
        
        public static AdminScenarioListDto.ResponseList from(Scenario scenario) {
            return AdminScenarioListDto.ResponseList.builder()
            		.scenarioId(scenario.getScenarioId())
                    .scenarioDescription(scenario.getScenarioDescription())
                    .category(scenario.getCategory())
                    .completeExp(scenario.getCompleteExp())
                    .build();
        }
    }
}