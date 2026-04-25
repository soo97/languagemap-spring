package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.StudyScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyScoreRepository extends JpaRepository<StudyScore, Long> {

    List<StudyScore> findAllByStudyLog_UserId(Long userId);
}
