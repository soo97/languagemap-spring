package kr.co.mapspring.ai.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.mapspring.ai.entity.CoachingMessage;

public interface CoachingMessageRepository extends JpaRepository<CoachingMessage, Long> {

    List<CoachingMessage> findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(Long coachingSessionId);
}