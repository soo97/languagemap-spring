package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.Mission;
import lombok.Builder;
import lombok.Getter;

public class UserMissionListDto {

    @Builder
    @Getter
    @Schema(name = "UserMissionListResponse", description = "미션 리스트 조회 DTO")
    public static class ResponseList {

        @Schema(
                description = "미션 제목",
                example = "커피 주문하기"
        		)
        private String missionTitle;

        @Schema(
                description = "미션 설명",
                example = "카페에서 원하는 음료를 영어로 주문한다."
        		)
        private String missionDescription;

        public static ResponseList from(Mission mission) {
            return ResponseList.builder()
                    .missionTitle(mission.getMissionTitle())
                    .missionDescription(mission.getMissionDescription())
                    .build();
        }
    }
}