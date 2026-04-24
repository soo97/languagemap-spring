package kr.co.mapspring.ai.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;

public interface CoachingSessionRepository extends JpaRepository<CoachingSession, Long> {

    Optional<CoachingSession> findByLearningSession_SessionIdAndCoachingSessionStatus(
            Long sessionId,
            CoachingSessionStatus coachingSessionStatus
    );
}