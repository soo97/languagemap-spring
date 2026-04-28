package kr.co.mapspring.social.repository;

import kr.co.mapspring.social.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
}
