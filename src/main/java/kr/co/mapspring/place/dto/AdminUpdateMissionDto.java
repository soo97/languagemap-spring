package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class AdminUpdateMissionDto {

    @Getter
    @Builder
    @Schema(description = "미션 수정 요청 DTO")
    public static class RequestUpdate {

        @Schema(
                description = "수정할 미션 제목",
                example = "카페 주문 미션"
        )
        private String missionTitle;

        @Schema(
                description = "수정할 미션 설명",
                example = "커피를 주문하는 상황을 연습한다."
        )
        private String missionDescription;

        @Schema(
                description = "수정할 시나리오 ID",
                example = "2"
        )
        private Long scenarioId;
    }
}