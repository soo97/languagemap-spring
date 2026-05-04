package kr.co.mapspring.ai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.ai.entity.CoachingScriptTurn;

public interface CoachingScriptTurnRepository extends JpaRepository<CoachingScriptTurn, Long> {

    Optional<CoachingScriptTurn> findByCoachingSession_CoachingSessionIdAndTurnOrder(
            Long coachingSessionId,
            Integer turnOrder
    );

    List<CoachingScriptTurn> findByCoachingSession_CoachingSessionIdOrderByTurnOrderAsc(
            Long coachingSessionId
    );
}