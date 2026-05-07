package kr.co.mapspring.ai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.ai.entity.CoachingPronunciationResult;

public interface CoachingPronunciationResultRepository
        extends JpaRepository<CoachingPronunciationResult, Long> {

    Optional<CoachingPronunciationResult> findByCoachingMessage_CoachingMessageId(
            Long coachingMessageId
    );

    List<CoachingPronunciationResult> findByCoachingScriptTurn_CoachingSession_CoachingSessionIdOrderByCreatedAtAsc(
            Long coachingSessionId
    );
}