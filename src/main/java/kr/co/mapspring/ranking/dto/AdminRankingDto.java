package kr.co.mapspring.ranking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class AdminRankingDto {

    @Getter
    @Builder
    @Schema(description = "관리자 전체 사용자 수 응답 DTO")
    public static class ResponseTotalUserCount {

        @Schema(description = "전체 사용자 수", example = "10")
        private Long totalUserCount;

        public static ResponseTotalUserCount from(Long totalUserCount) {
            return ResponseTotalUserCount.builder()
                    .totalUserCount(totalUserCount)
                    .build();
        }
    }
}
