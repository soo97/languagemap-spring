package kr.co.mapspring.payment.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.payment.entity.Payment;
import kr.co.mapspring.payment.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminPaymentDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "관리자 결제 목록 응답 DTO")
    public static class ResponseList {
        private Long paymentId;
        private String userName;
        private String userEmail;
        private String impUid;
        private String merchantUid;
        private Integer paymentAmount;
        private String paymentMethod;
        private String paymentStatus;
        private LocalDateTime paymentAt;
        private String planType;
        private String planStatus;
        private LocalDateTime planStartAt;
        private LocalDateTime planEndAt;

        public static ResponseList from(Payment payment, Subscription subscription) {
            return ResponseList.builder()
                    .paymentId(payment.getPaymentId())
                    .userName(payment.getUser().getName())
                    .userEmail(payment.getUser().getEmail())
                    .impUid(payment.getImpUid())
                    .merchantUid(payment.getMerchantUid())
                    .paymentAmount(payment.getPaymentAmount())
                    .paymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null)
                    .paymentStatus(payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null)
                    .paymentAt(payment.getPaymentAt())
                    .planType(subscription != null ? subscription.getPlanType().name() : null)
                    .planStatus(subscription != null ? subscription.getPlanStatus().name() : null)
                    .planStartAt(subscription != null ? subscription.getPlanStartAt() : null)
                    .planEndAt(subscription != null ? subscription.getPlanEndAt() : null)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "관리자 결제 통계 응답 DTO")
    public static class ResponseStats {
        private long totalPaymentCount;
        private long successPaymentCount;
        private long activeSubscriptionCount;
        private long totalAmount;
    }
}