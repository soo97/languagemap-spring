package kr.co.mapspring.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.MissionSession;

public interface MissionSessionRepository extends JpaRepository<MissionSession, Long>{

	Optional<MissionSession> findByLearningSession_SessionIdAndMission_MissionId(Long sessionId, Long missionId);
}
