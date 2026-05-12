package kr.co.mapspring.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.LearningSession;

public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {

    @EntityGraph(attributePaths = {"place", "place.region", "place.scenario"})
    Optional<LearningSession> findBySessionId(Long sessionId);
    
    Optional<LearningSession> findBySessionIdAndUser_UserId(
            Long sessionId,
            Long userId
    );
    
    Optional<LearningSession> findByUser_UserIdAndPlace_PlaceId(
            Long userId,
            Long placeId
    );

    List<LearningSession> findByUser_UserId(Long userId);
    
    List<LearningSession> findTop2ByUser_UserIdAndEndTimeIsNotNullOrderByEndTimeDesc(Long userId);
}