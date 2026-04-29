package kr.co.mapspring.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class AdminUpdatePlaceDto {

    @Getter
    @Builder
    @Schema(description = "장소 수정 요청 DTO")
    public static class RequestUpdate {

        @Schema(
                description = "수정할 장소 이름",
                example = "스타벅스 강남점"
        )
        private String placeName;

        @Schema(
                description = "수정할 장소 설명",
                example = "조용한 분위기의 카페"
        )
        private String placeDescription;

        @Schema(
                description = "수정할 시나리오 ID",
                example = "2"
        )
        private Long scenarioId;
    }
}