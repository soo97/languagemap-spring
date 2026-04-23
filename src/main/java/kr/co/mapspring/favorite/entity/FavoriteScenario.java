package kr.co.mapspring.favorite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "favorite_scenario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_favorite_scenario_user_scenario", columnNames = {"user_id", "scenario_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_scenario_id", nullable = false, updatable = false)
    private Long favoriteScenarioId;

//    TODO: 추후 엔티티 확정 시 연관관계로 매핑 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id", nullable = false)
    private Long userId;

//    TODO: 추후 엔티티 확정 시 연관관계로 매핑 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "scenario_id", nullable = false)
    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static FavoriteScenario create(Long userId, Long scenarioId) {
        FavoriteScenario favoriteScenario = new FavoriteScenario();
        favoriteScenario.userId = userId;
        favoriteScenario.scenarioId = scenarioId;
        return favoriteScenario;
    }

    @PrePersist
    protected void perPersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 테스트 전용 메서드
    public static FavoriteScenario of(Long favoriteScenarioId, Long userId, Long scenarioId) {
        FavoriteScenario favoriteScenario = new FavoriteScenario();
        favoriteScenario.favoriteScenarioId = favoriteScenarioId;
        favoriteScenario.userId = userId;
        favoriteScenario.scenarioId = scenarioId;
        favoriteScenario.createdAt = LocalDateTime.now();
        return favoriteScenario;
    }
}
