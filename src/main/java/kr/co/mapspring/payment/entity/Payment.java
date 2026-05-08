package kr.co.mapspring.payment.entity;

import jakarta.persistence.*;
import kr.co.mapspring.payment.enums.PaymentMethod;
import kr.co.mapspring.payment.enums.PaymentStatus;
import kr.co.mapspring.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "imp_uid", unique = true, length = 100)
    private String impUid;

    @Column(name = "merchant_uid", unique = true, length = 100)
    private String merchantUid;

    @Column(name = "payment_amount")
    private Integer paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 30)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_at")
    private LocalDateTime paymentAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    private Payment(User user, String impUid, String merchantUid,
                    Integer paymentAmount, PaymentMethod paymentMethod,
                    PaymentStatus paymentStatus, LocalDateTime paymentAt) {
        this.user = user;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentAt = paymentAt;
    }

    public static Payment create(User user, String impUid, String merchantUid,
                                  Integer paymentAmount, PaymentMethod paymentMethod) {
        return Payment.builder()
                .user(user)
                .impUid(impUid)
                .merchantUid(merchantUid)
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.SUCCESS)
                .paymentAt(LocalDateTime.now())
                .build();
    }
}