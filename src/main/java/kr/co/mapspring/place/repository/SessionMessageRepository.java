package kr.co.mapspring.place.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.SessionMessageRole;

public interface SessionMessageRepository extends JpaRepository<SessionMessage, Long> {

    List<SessionMessage> findBySession_SessionIdOrderByCreatedAtAsc(Long sessionId);
    
    List<SessionMessage> findByMissionSession_MissionSessionIdOrderByCreatedAtAsc(Long missionSessionId);
    
    long countByMissionSession_MissionSessionIdAndRole(
            Long missionSessionId,
            SessionMessageRole role
    );
}