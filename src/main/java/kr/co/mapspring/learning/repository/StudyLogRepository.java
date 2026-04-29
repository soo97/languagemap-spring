package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {

    List<StudyLog> findAllByUser_UserId(Long userId);
}
