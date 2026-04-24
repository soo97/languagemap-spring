package kr.co.mapspring.ai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.ai.entity.CoachingSession;

public interface CoachingSessionRepository extends JpaRepository<CoachingSession, Long> {

    boolean existsByLearningSession_SessionId(Long sessionId);

    Optional<CoachingSession> findByLearningSession_SessionId(Long sessionId);
}