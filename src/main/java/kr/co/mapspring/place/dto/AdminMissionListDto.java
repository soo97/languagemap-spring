package kr.co.mapspring.place.dto;

import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import lombok.Builder;
import lombok.Getter;

public class AdminMissionListDto {

    @Getter
    @Builder
    @Schema(name = "AdminMissionListResponse", description = "미션 리스트 응답 DTO")
    public static class ResponseList {
    	
    	@Schema(
                description = "미션 ID",
                example = "1"
        )
        private Long missionId;

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

        public static ResponseList from(Mission mission) {
            return ResponseList.builder()
                    .missionId(mission.getMissionId())
                    .missionTitle(mission.getMissionTitle())
                    .missionDescription(mission.getMissionDescription())
                    .scenarioId(
                            Optional.ofNullable(mission.getScenario())
                                    .map(scenario -> scenario.getScenarioId())
                                    .orElse(null)
                    )
                    .build();
        }
    }
}