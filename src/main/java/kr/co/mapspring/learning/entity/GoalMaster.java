package kr.co.mapspring.learning.entity;

import jakarta.persistence.*;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "goal_master")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GoalMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_master_id")
    private Long goalMasterId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "badge_id", nullable = false)
    @Column(name = "badge_id", nullable = false)
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

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder.Default
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
