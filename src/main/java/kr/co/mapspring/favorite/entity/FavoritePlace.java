package kr.co.mapspring.favorite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePlace {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "favorite_place_id", nullable = false, updatable = false)
    private Long favoritePlaceId;

//    TODO: 추후 엔티티 확정 시 연관관계로 매핑 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id", nullable = false)
    private Long userId;

//    TODO: 추후 Place 엔티티 확정 시 연관관계로 변경 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "place_id", nullable = false)
    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static FavoritePlace create(Long userId, Long placeId) {
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.userId = userId;
        favoritePlace.placeId = placeId;
        return favoritePlace;
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
