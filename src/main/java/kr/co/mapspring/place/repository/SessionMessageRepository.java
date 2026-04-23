package kr.co.mapspring.place.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.SessionMessage;

public interface SessionMessageRepository extends JpaRepository<SessionMessage, Long> {

    List<SessionMessage> findBySession_SessionIdOrderByCreatedAtAsc(Long sessionId);
}