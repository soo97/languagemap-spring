package kr.co.mapspring.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "goal_master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_master_id", nullable = false, updatable = false)
    private Long goalMasterId;

//    TODO: 엔티티 확정 후 매핑 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "badge_id", nullable = false)
    @Column(name = "badge_id")
    private Long badgeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false, length = 50)
    private GoalType goalType;

    @Column(name = "goal_title", nullable = false, length = 100)
    private String goalTitle;

    @Column(name = "goal_description")
    private String goalDescription;

    @Column(name = "target_value", nullable = false)
    private Integer targetValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    private GoalPeriodType periodType;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static GoalMaster create(Long badgeId, GoalType goalType, String goalTitle, String goalDescription, Integer targetValue, GoalPeriodType periodType
    ) {
        GoalMaster goalMaster = new GoalMaster();
        goalMaster.badgeId = badgeId;
        goalMaster.goalType = goalType;
        goalMaster.goalTitle = goalTitle;
        goalMaster.goalDescription = goalDescription;
        goalMaster.targetValue = targetValue;
        goalMaster.periodType = periodType;
        goalMaster.isActive = true;
        return goalMaster;
    }

    // 테스트 전용 메서드
    public static GoalMaster of(Long goalMasterId, String goalTitle, GoalPeriodType periodType, GoalType goalType, Integer targetValue) {
        GoalMaster goalMaster = new GoalMaster();
        goalMaster.goalMasterId = goalMasterId;
        goalMaster.goalTitle = goalTitle;
        goalMaster.periodType = periodType;
        goalMaster.goalType = goalType;
        goalMaster.targetValue = targetValue;
        goalMaster.isActive = true;
        goalMaster.createdAt = LocalDateTime.now();
        goalMaster.updatedAt = LocalDateTime.now();
        return goalMaster;
    }
}
