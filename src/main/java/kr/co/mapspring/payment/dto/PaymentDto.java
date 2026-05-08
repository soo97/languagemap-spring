package kr.co.mapspring.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.mapspring.payment.entity.Payment;
import kr.co.mapspring.payment.entity.Subscription;
import kr.co.mapspring.payment.enums.PaymentMethod;
import kr.co.mapspring.payment.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PaymentDto {

    // 결제 검증 요청 DTO (프론트 → 백엔드)
    // 포트원 결제 완료 후 imp_uid, merchant_uid를 백엔드로 전달해 검증 요청
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "결제 검증 요청 DTO")
    public static class RequestVerify {

        @NotBlank(message = "imp_uid는 필수입니다.")
        @Schema(description = "포트원 결제 고유번호", example = "imp_123456789")
        private String impUid;

        @NotBlank(message = "merchant_uid는 필수입니다.")
        @Schema(description = "우리 서비스 주문번호", example = "order_20240101_123456")
        private String merchantUid;

        @NotNull(message = "플랜 타입은 필수입니다.")
        @Schema(description = "구독 플랜 타입", example = "MONTHLY")
        private PlanType planType;

        @NotNull(message = "결제 수단은 필수입니다.")
        @Schema(description = "결제 수단", example = "KAKAOPAY")
        private PaymentMethod paymentMethod;

        @NotNull(message = "결제 금액은 필수입니다.")
        @Schema(description = "결제 금액", example = "9900")
        private Integer paymentAmount;
    }

    // 결제 완료 응답 DTO (백엔드 → 프론트)
    // 결제 검증 성공 후 구독 정보와 결제 정보를 응답
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "결제 완료 응답 DTO")
    public static class ResponseVerify {

        @Schema(description = "결제 ID", example = "1")
        private Long paymentId;

        @Schema(description = "구독 ID", example = "1")
        private Long subscriptionId;

        @Schema(description = "구독 플랜 타입", example = "MONTHLY")
        private PlanType planType;

        @Schema(description = "결제 금액", example = "9900")
        private Integer paymentAmount;

        @Schema(description = "결제 수단", example = "KAKAOPAY")
        private PaymentMethod paymentMethod;

        @Schema(description = "구독 시작일시")
        private LocalDateTime planStartAt;

        @Schema(description = "구독 종료일시")
        private LocalDateTime planEndAt;

        public static ResponseVerify from(Payment payment, Subscription subscription) {
            return ResponseVerify.builder()
                    .paymentId(payment.getPaymentId())
                    .subscriptionId(subscription.getSubscriptionId())
                    .planType(subscription.getPlanType())
                    .paymentAmount(payment.getPaymentAmount())
                    .paymentMethod(payment.getPaymentMethod())
                    .planStartAt(subscription.getPlanStartAt())
                    .planEndAt(subscription.getPlanEndAt())
                    .build();
        }
    }

    // 현재 구독 상태 응답 DTO
    // 사용자의 현재 활성 구독 정보를 조회할 때 사용
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "구독 상태 응답 DTO")
    public static class ResponseSubscription {

        @Schema(description = "구독 ID", example = "1")
        private Long subscriptionId;

        @Schema(description = "구독 플랜 타입", example = "MONTHLY")
        private PlanType planType;

        @Schema(description = "구독 상태", example = "ACTIVE")
        private String planStatus;

        @Schema(description = "구독 시작일시")
        private LocalDateTime planStartAt;

        @Schema(description = "구독 종료일시")
        private LocalDateTime planEndAt;

        public static ResponseSubscription from(Subscription subscription) {
            return ResponseSubscription.builder()
                    .subscriptionId(subscription.getSubscriptionId())
                    .planType(subscription.getPlanType())
                    .planStatus(subscription.getPlanStatus().name())
                    .planStartAt(subscription.getPlanStartAt())
                    .planEndAt(subscription.getPlanEndAt())
                    .build();
        }
    }
}