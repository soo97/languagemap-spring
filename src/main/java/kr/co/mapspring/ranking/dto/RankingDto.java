package kr.co.mapspring.ranking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RankingDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "랭킹 조회 응답 DTO")
    public static class ResponseRanking {

        @Schema(description = "순위", example = "1")
        private int rank;

        @Schema(description = "사용자 ID", example = "3")
        private Long userId;

        @Schema(description = "총 점수", example = "950")
        private Long totalScore;

        @Builder
        public ResponseRanking(int rank, Long userId, Long totalScore) {
            this.rank = rank;
            this.userId = userId;
            this.totalScore = totalScore;
        }

        public static ResponseRanking from(int rank, Long userId, Long totalScore) {
            return ResponseRanking.builder()
                    .rank(rank)
                    .userId(userId)
                    .totalScore(totalScore)
                    .build();
        }
    }

}
