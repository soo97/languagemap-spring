package kr.co.mapspring.favorite.dto;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class FavoritePlaceDto {

    @Getter
    @Builder
    public static class RequestAddFavoritePlace {
        private Long userId;
        private Long placeId;
    }

    @Getter
    @Builder
    public static class RequestRemoveFavoritePlace {
        private Long userId;
        private Long placeId;
    }

    @Getter
    @Builder
    public static class ResponseFavoritePlace {
        private Long favoritePlaceId;
        private Long userId;
        private Long placeId;
        private LocalDateTime createdAt;

        public static ResponseFavoritePlace from(FavoritePlace favoritePlace) {
            return ResponseFavoritePlace.builder()
                    .favoritePlaceId(favoritePlace.getFavoritePlaceId())
                    .userId(favoritePlace.getUserId())
                    .placeId(favoritePlace.getPlaceId())
                    .createdAt(favoritePlace.getCreatedAt())
                    .build();
        }
    }

}
