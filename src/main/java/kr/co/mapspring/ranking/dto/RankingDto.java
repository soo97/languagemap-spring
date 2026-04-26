package kr.co.mapspring.ranking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RankingDto {

    @Getter
    @NoArgsConstructor
    public static class ResponseRanking {
        private int rank;
        private Long userId;
        private Long totalScore;

        @Builder
        public ResponseRanking(int rank, Long userId, Long totalScore) {
            this.rank = rank;
            this.userId = userId;
            this.totalScore = totalScore;
        }
    }

    public static ResponseRanking from(int rank, Long userId, Long totalScore) {
        return ResponseRanking.builder()
                .rank(rank)
                .userId(userId)
                .totalScore(totalScore)
                .build();
    }
}
