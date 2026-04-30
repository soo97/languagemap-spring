package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.StudyScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyScoreRepository extends JpaRepository<StudyScore, Long> {

    List<StudyScore> findAllByStudyLog_User_UserId(Long userId);

    Optional<StudyScore> findByStudyLog_StudyLogId(Long studyLogId);
}
