package kr.co.mapspring.learning.repository;

import kr.co.mapspring.learning.entity.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
}
