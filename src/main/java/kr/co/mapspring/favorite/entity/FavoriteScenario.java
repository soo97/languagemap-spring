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
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.user.entity.User;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static FavoriteScenario create(User user, Scenario scenario) {
        FavoriteScenario favoriteScenario = new FavoriteScenario();
        favoriteScenario.user = user;
        favoriteScenario.scenario = scenario;
        return favoriteScenario;
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 테스트 전용 메서드
    public static FavoriteScenario of(Long favoriteScenarioId, User user, Scenario scenario) {
        FavoriteScenario favoriteScenario = new FavoriteScenario();
        favoriteScenario.favoriteScenarioId = favoriteScenarioId;
        favoriteScenario.user = user;
        favoriteScenario.scenario = scenario;
        favoriteScenario.createdAt = LocalDateTime.now();
        return favoriteScenario;
    }
}
