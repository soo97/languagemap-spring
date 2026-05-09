package kr.co.mapspring.payment.entity;

import jakarta.persistence.*;
import kr.co.mapspring.payment.enums.PlanType;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, length = 20)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status", nullable = false, length = 20)
    private SubscriptionStatus planStatus;

    @Column(name = "plan_start_at", nullable = false)
    private LocalDateTime planStartAt;

    @Column(name = "plan_end_at", nullable = false)
    private LocalDateTime planEndAt;

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
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    private Subscription(Payment payment, PlanType planType,
                          SubscriptionStatus planStatus,
                          LocalDateTime planStartAt, LocalDateTime planEndAt) {
        this.payment = payment;
        this.planType = planType;
        this.planStatus = planStatus;
        this.planStartAt = planStartAt;
        this.planEndAt = planEndAt;
    }

    public static Subscription create(Payment payment, PlanType planType) {
        LocalDateTime now = LocalDateTime.now();
        return Subscription.builder()
                .payment(payment)
                .planType(planType)
                .planStatus(SubscriptionStatus.ACTIVE)
                .planStartAt(now)
                .planEndAt(planType == PlanType.MONTHLY ? now.plusMonths(1) : now.plusYears(1))
                .build();
    }

    public void expire() {
        this.planStatus = SubscriptionStatus.EXPIRED;
    }

    public void cancel() {
        this.planStatus = SubscriptionStatus.CANCELLED;
    }
}