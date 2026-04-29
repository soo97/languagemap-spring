package kr.co.mapspring.social.repository;

import kr.co.mapspring.social.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    List<UserReport> findByReporter_UserId(Long userId);
}
