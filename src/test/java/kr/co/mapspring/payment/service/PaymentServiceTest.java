package kr.co.mapspring.payment.service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.payment.dto.PaymentDto;
import kr.co.mapspring.payment.entity.Payment;
import kr.co.mapspring.payment.entity.Subscription;
import kr.co.mapspring.payment.enums.PaymentMethod;
import kr.co.mapspring.payment.enums.PlanType;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.payment.repository.PaymentRepository;
import kr.co.mapspring.payment.repository.SubscriptionRepository;
import kr.co.mapspring.payment.service.impl.PaymentServiceImpl;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    // 테스트용 유저
    private User testUser;

    @BeforeEach
    void setUp() {
        // @Value로 주입되는 포트원 키값 테스트용으로 세팅
        ReflectionTestUtils.setField(paymentService, "impKey", "test_imp_key");
        ReflectionTestUtils.setField(paymentService, "impSecret", "test_imp_secret");

        testUser = User.builder()
                .email("test@test.com")
                .name("테스트")
                .passwordHash("hash")
                .build();
    }

    // ==================== verifyAndSave ====================

    @Test
    @DisplayName("결제 검증 - 유저가 존재하지 않으면 예외 발생")
    void verifyAndSave_userNotFound_throwsException() {
        // given
        Long userId = 999L;
        PaymentDto.RequestVerify request = createRequest();

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.verifyAndSave(userId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("결제 검증 - 이미 처리된 imp_uid면 예외 발생")
    void verifyAndSave_duplicateImpUid_throwsException() {
        // given
        Long userId = 1L;
        PaymentDto.RequestVerify request = createRequest();

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        // 이미 존재하는 imp_uid
        given(paymentRepository.existsByImpUid(request.getImpUid())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> paymentService.verifyAndSave(userId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.PAYMENT_ALREADY_EXISTS.getMessage());

        // 중복 결제 감지 시 저장 로직은 실행되면 안 됨
        verify(paymentRepository, never()).save(any());
        verify(subscriptionRepository, never()).save(any());
    }

    // ==================== getActiveSubscription ====================

    @Test
    @DisplayName("구독 조회 - 유저가 존재하지 않으면 예외 발생")
    void getActiveSubscription_userNotFound_throwsException() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.getActiveSubscription(userId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("구독 조회 - 활성 구독이 없으면 예외 발생")
    void getActiveSubscription_noActiveSubscription_throwsException() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(subscriptionRepository.findByPayment_UserAndPlanStatus(any(), any()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.getActiveSubscription(userId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SUBSCRIPTION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("구독 조회 - 활성 구독이 있으면 정상 반환")
    void getActiveSubscription_success() {
        // given
        Long userId = 1L;
        Payment payment = Payment.create(
                testUser, "imp_test", "order_test", 9900, PaymentMethod.KAKAOPAY
        );
        Subscription subscription = Subscription.create(payment, PlanType.MONTHLY);

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(subscriptionRepository.findByPayment_UserAndPlanStatus(any(), any()))
                .willReturn(Optional.of(subscription));

        // when
        PaymentDto.ResponseSubscription response = paymentService.getActiveSubscription(userId);

        // then
        assertThat(response.getPlanType()).isEqualTo(PlanType.MONTHLY);
        assertThat(response.getPlanStatus()).isEqualTo(SubscriptionStatus.ACTIVE.name());
    }

    // ==================== cancelSubscription ====================

    @Test
    @DisplayName("구독 취소 - 유저가 존재하지 않으면 예외 발생")
    void cancelSubscription_userNotFound_throwsException() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.cancelSubscription(userId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("구독 취소 - 활성 구독이 없으면 예외 발생")
    void cancelSubscription_noActiveSubscription_throwsException() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(subscriptionRepository.findByPayment_UserAndPlanStatus(any(), any()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.cancelSubscription(userId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SUBSCRIPTION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("구독 취소 - 정상 취소 처리")
    void cancelSubscription_success() {
        // given
        Long userId = 1L;
        Payment payment = Payment.create(
                testUser, "imp_test", "order_test", 9900, PaymentMethod.KAKAOPAY
        );
        Subscription subscription = Subscription.create(payment, PlanType.MONTHLY);

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(subscriptionRepository.findByPayment_UserAndPlanStatus(any(), any()))
                .willReturn(Optional.of(subscription));

        // when
        paymentService.cancelSubscription(userId);

        // then - 구독 상태가 CANCELLED로 변경됐는지 확인
        assertThat(subscription.getPlanStatus()).isEqualTo(SubscriptionStatus.CANCELLED);
    }

    // ==================== 헬퍼 메서드 ====================

    // 테스트용 결제 요청 DTO 생성
    private PaymentDto.RequestVerify createRequest() {
        return new PaymentDto.RequestVerify(
                "imp_test_123",
                "order_20240101_123",
                PlanType.MONTHLY,
                PaymentMethod.KAKAOPAY,
                9900
        );
    }
}