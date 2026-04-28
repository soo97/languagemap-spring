package kr.co.mapspring.favorite.repository;

import kr.co.mapspring.favorite.entity.FavoriteScenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteScenarioRepository extends JpaRepository<FavoriteScenario, Long> {

    boolean existsByUser_UserIdAndScenario_ScenarioId(Long userId, Long scenarioId);

    Optional<FavoriteScenario> findByUser_UserIdAndScenario_ScenarioId(Long userId, Long scenarioId);

    List<FavoriteScenario> findAllByUser_UserId(Long userId);
}