package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.StudyScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyScoreRepository extends JpaRepository<StudyScore, Long> {
}
