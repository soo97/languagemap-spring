package kr.co.mapspring.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.favorite.entity.FavoriteScenario;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FavoriteScenarioDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "시나리오 즐겨찾기 추가 요청 DTO")
    public static class RequestAddFavoriteScenario {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "즐겨찾기할 시나리오 ID", example = "5")
        private Long scenarioId;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "시나리오 즐겨찾기 삭제 요청 DTO")
    public static class RequestRemoveFavoriteScenario {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "삭제할 시나리오 ID", example = "5")
        private Long scenarioId;
    }

    @Getter
    @Builder
    @Schema(description = "시나리오 즐겨찾기 응답 DTO")
    public static class ResponseFavoriteScenario {

        @Schema(description = "즐겨찾기 시나리오 ID", example = "1")
        private Long favoriteScenarioId;

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "시나리오 ID", example = "5")
        private Long scenarioId;

        @Schema(description = "즐겨찾기 등록일시", example = "2026-04-24T12:00:00")
        private LocalDateTime createdAt;

        public static FavoriteScenarioDto.ResponseFavoriteScenario from(FavoriteScenario favoriteScenario) {
            return FavoriteScenarioDto.ResponseFavoriteScenario.builder()
                    .favoriteScenarioId(favoriteScenario.getFavoriteScenarioId())
                    .userId(favoriteScenario.getUser().getUserId())
                    .scenarioId(favoriteScenario.getScenario().getScenarioId())
                    .createdAt(favoriteScenario.getCreatedAt())
                    .build();
        }
    }
}
