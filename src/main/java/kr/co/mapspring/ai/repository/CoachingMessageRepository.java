package kr.co.mapspring.ai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.enums.CoachingMessageRole;

public interface CoachingMessageRepository extends JpaRepository<CoachingMessage, Long> {

    List<CoachingMessage> findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(Long coachingSessionId);
    
    long countByCoachingSession_LearningSession_User_UserIdAndRoleAndCreatedAtBetween(
            Long userId,
            CoachingMessageRole role,
            LocalDateTime start,
            LocalDateTime end
    );
}