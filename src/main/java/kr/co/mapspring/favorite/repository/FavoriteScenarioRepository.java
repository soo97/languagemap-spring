package kr.co.mapspring.favorite.repository;

import kr.co.mapspring.favorite.entity.FavoriteScenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteScenarioRepository extends JpaRepository<FavoriteScenario, Long> {

    boolean existsByUserIdAndScenarioId(Long userId, Long scenarioId);

    Optional<FavoriteScenario> findByUserIdAndScenarioId(Long userId, Long scenarioId);

    List<FavoriteScenario> findAllByUserId(Long userId);
}
