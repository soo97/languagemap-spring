package kr.co.mapspring.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.enums.MissionSessionStatus;

public interface MissionSessionRepository extends JpaRepository<MissionSession, Long>{

	Optional<MissionSession> findByLearningSession_SessionIdAndMission_MissionId(Long sessionId, Long missionId);
	
	Optional<MissionSession> findByLearningSession_SessionIdAndMissionStatus(Long sessionId, MissionSessionStatus missionStatus);
	
	boolean existsByLearningSession_SessionIdAndMissionStatusNot(Long sessionId, MissionSessionStatus missionStats);
}
