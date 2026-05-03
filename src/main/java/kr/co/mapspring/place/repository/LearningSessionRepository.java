package kr.co.mapspring.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.enums.MissionSessionStatus;

public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {

    @EntityGraph(attributePaths = {"place", "place.region", "place.scenario"})
    Optional<LearningSession> findBySessionId(Long sessionId);
    
    boolean existsByLearningSession_SessionIdAndMissionStatusNot(Long sessionId, MissionSessionStatus missionStatus);
    
}