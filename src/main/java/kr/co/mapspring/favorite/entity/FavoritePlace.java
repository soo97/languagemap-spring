package kr.co.mapspring.favorite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "favorite_place",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_favorite_place_user_place", columnNames = {"user_id", "place_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePlace {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "favorite_place_id", nullable = false, updatable = false)
    private Long favoritePlaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static FavoritePlace create(User user, Place place) {
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.user = user;
        favoritePlace.place = place;
        return favoritePlace;
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 테스트 전용 메서드
    public static FavoritePlace of(Long favoritePlaceId, User user, Place place) {
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.favoritePlaceId = favoritePlaceId;
        favoritePlace.user = user;
        favoritePlace.place = place;
        favoritePlace.createdAt = LocalDateTime.now();
        return favoritePlace;
    }


}
