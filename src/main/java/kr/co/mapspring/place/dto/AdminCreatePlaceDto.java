package kr.co.mapspring.place.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminCreatePlaceDto {

	@Getter
	@Builder // 테스트 코드용
	@AllArgsConstructor
	@NoArgsConstructor
    @Schema(name = "AdminCreatePlaceRequest", description = "장소 생성 요청 DTO")
    public static class RequestCreate {

        @Schema(
                description = "구글 Place 고유 ID",
                example = "ChIJN1t_tDeuEmsRUsoyG83frY4"
        		)
        private String googlePlaceId;

        @Schema(
                description = "장소 이름",
                example = "스타벅스 강남점"
        		)
        private String placeName;

        @Schema(
                description = "장소 주소",
                example = "서울특별시 강남구 테헤란로 123"
        		)
        private String placeAddress;

        @Schema(
                description = "위도",
                example = "37.4979"
        )
        private BigDecimal latitude;

        @Schema(
                description = "경도",
                example = "127.0276"
        )
        private BigDecimal longitude;

        @Schema(
                description = "장소 설명",
                example = "조용한 분위기의 카페"
        )
        private String placeDescription;

        @Schema(
                description = "연결된 시나리오 ID",
                example = "1"
        )
        private Long scenarioId;

        @Schema(
                description = "지역 ID",
                example = "10"
        )
        private Long regionId;
    }
}