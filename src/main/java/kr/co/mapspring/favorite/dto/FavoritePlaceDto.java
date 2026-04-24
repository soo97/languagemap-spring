package kr.co.mapspring.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.favorite.entity.FavoritePlace;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public class FavoritePlaceDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "장소 즐겨찾기 추가 요청 DTO")
    public static class RequestAddFavoritePlace {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "즐겨찾기할 장소 ID", example = "10")
        private Long placeId;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "장소 즐겨찾기 삭제 요청 DTO")
    public static class RequestRemoveFavoritePlace {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "삭제할 장소 ID", example = "10")
        private Long placeId;
    }

    @Getter
    @Builder
    @Schema(description = "장소 즐겨찾기 응답 DTO")
    public static class ResponseFavoritePlace {

        @Schema(description = "즐겨찾기 장소 ID", example = "1")
        private Long favoritePlaceId;

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "장소 ID", example = "10")
        private Long placeId;

        @Schema(description = "즐겨찾기 등록일시", example = "2026-04-24T12:00:00")
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
