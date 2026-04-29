package kr.co.mapspring.social.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.mapspring.social.enums.ReportStatus;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false, updatable = false)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reportedUser;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReportStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "admin_memo", length = 255)
    private String adminMemo;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static UserReport create(User reporter, User reportedUser, String reason) {
        UserReport userReport = new UserReport();
        userReport.reporter = reporter;
        userReport.reportedUser = reportedUser;
        userReport.reason = reason;
        userReport.status = ReportStatus.PENDING;
        return userReport;
    }

    public void resolve(String adminMemo) {
        this.status = ReportStatus.RESOLVED;
        this.adminMemo = adminMemo;
        this.processedAt = LocalDateTime.now();
    }

    public void reject(String adminMemo) {
        this.status = ReportStatus.REJECTED;
        this.adminMemo = adminMemo;
        this.processedAt = LocalDateTime.now();
    }

    // 테스트 전용 생성 메서드
    public static UserReport of(
            Long reportId,
            User reporter,
            User reportedUser,
            String reason,
            ReportStatus status
    ) {
        UserReport userReport = new UserReport();
        userReport.reportId = reportId;
        userReport.reporter = reporter;
        userReport.reportedUser = reportedUser;
        userReport.reason = reason;
        userReport.status = status;
        userReport.createdAt = LocalDateTime.now();
        return userReport;
    }


}
