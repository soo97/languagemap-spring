package kr.co.mapspring.ai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.ai.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByCoachingSession_CoachingSessionId(Long coachingSessionId);
}