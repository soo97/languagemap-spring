package kr.co.mapspring.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.SessionEvaluation;

public interface SessionEvaluationRepository extends JpaRepository<SessionEvaluation, Long> {

    Optional<SessionEvaluation> findBySession_SessionId(Long sessionId);
}