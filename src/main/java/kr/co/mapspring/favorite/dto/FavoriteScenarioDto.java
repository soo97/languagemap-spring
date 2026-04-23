package kr.co.mapspring.favorite.dto;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import kr.co.mapspring.favorite.entity.FavoriteScenario;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class FavoriteScenarioDto {

//    TODO: 스웨거 문서 작성
    @Getter
    @Builder
    public static class RequestAddFavoriteScenario {
        private Long userId;
        private Long scenarioId;
    }

    @Getter
    @Builder
    public static class RequestRemoveFavoriteScenario {
        private Long userId;
        private Long scenarioId;
    }

    @Getter
    @Builder
    public static class ResponseFavoriteScenario {
        private Long favoriteScenarioId;
        private Long userId;
        private Long scenarioId;
        private LocalDateTime createdAt;

        public static FavoriteScenarioDto.ResponseFavoriteScenario from(FavoriteScenario favoriteScenario) {
            return FavoriteScenarioDto.ResponseFavoriteScenario.builder()
                    .favoriteScenarioId(favoriteScenario.getFavoriteScenarioId())
                    .userId(favoriteScenario.getUserId())
                    .scenarioId(favoriteScenario.getScenarioId())
                    .createdAt(favoriteScenario.getCreatedAt())
                    .build();
        }
    }
}
