package kr.co.mapspring.ai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.ai.entity.CoachingFeedback;

public interface CoachingFeedbackRepository extends JpaRepository<CoachingFeedback, Long> {

    Optional<CoachingFeedback> findByCoachingSession_CoachingSessionId(Long coachingSessionId);

    boolean existsByCoachingSession_CoachingSessionId(Long coachingSessionId);
}