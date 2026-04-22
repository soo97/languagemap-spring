package kr.co.mapspring.learning.entity;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import kr.co.mapspring.learning.enums.UserGoalStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_goal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_goal_id", nullable = false, updatable = false)
    private Long userGoalId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_master_id", nullable = false)
    private GoalMaster goalMaster;

    @Column(name = "current_value", nullable = false)
    private Integer currentValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserGoalStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = UserGoalStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = UserGoalStatus.FAILED;
    }

    public void cancel() {
        this.status = UserGoalStatus.CANCELED;
    }

    // 테스트 전용 생성 메서드
    public static UserGoal of(Long userId, GoalMaster goalMaster,
                              LocalDate startDate, LocalDate endDate) {

        UserGoal userGoal = new UserGoal();
        userGoal.userId = userId;
        userGoal.goalMaster = goalMaster;
        userGoal.currentValue = 0;
        userGoal.status = UserGoalStatus.ACTIVE;
        userGoal.startDate = startDate;
        userGoal.endDate = endDate;

        return userGoal;
    }




}
