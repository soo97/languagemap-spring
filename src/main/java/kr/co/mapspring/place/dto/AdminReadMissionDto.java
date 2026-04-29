package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

public class AdminReadMissionDto {

    @Getter
    @Builder
    @Schema(description = "미션 상세 조회 응답 DTO")
    public static class ResponseRead {

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
                description = "미션 상태 (ACTIVE, INACTIVE 등)",
                example = "ACTIVE"
        )
        private MissionStatus missionStatus;

        @Schema(
                description = "연결된 시나리오 ID",
                example = "1"
        )
        private Long scenarioId;

        public static AdminReadMissionDto.ResponseRead from(Mission mission) {
            return AdminReadMissionDto.ResponseRead.builder()
                    .missionTitle(mission.getMissionTitle())
                    .missionDescription(mission.getMissionDescription())
                    .missionStatus(mission.getMissionStatus())
                    .scenarioId(Optional.ofNullable(mission.getScenario())
                            .map(scenario -> scenario.getScenarioId())
                            .orElse(null))
                    .build();
        }
    }
}