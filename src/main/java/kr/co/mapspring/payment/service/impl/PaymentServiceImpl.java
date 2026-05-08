package kr.co.mapspring.payment.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.payment.dto.PaymentDto;
import kr.co.mapspring.payment.entity.Payment;
import kr.co.mapspring.payment.entity.Subscription;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.payment.repository.PaymentRepository;
import kr.co.mapspring.payment.repository.SubscriptionRepository;
import kr.co.mapspring.payment.service.PaymentService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Value("${portone.imp-key}")
    private String impKey;

    @Value("${portone.imp-secret}")
    private String impSecret;
    
    
    

    @Override
    @Transactional
    public PaymentDto.ResponseVerify verifyAndSave(Long userId, PaymentDto.RequestVerify request) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2. 중복 결제 방지 - imp_uid 중복 체크
        if (paymentRepository.existsByImpUid(request.getImpUid())) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }

        // 3. 포트원 서버에 결제 검증 요청
        // 프론트에서 받은 imp_uid로 포트원 서버에 실제 결제 정보를 조회해 위변조 방지
        try {
            IamportClient iamportClient = new IamportClient(impKey, impSecret);

            // << 변경: 풀네임으로 사용해서 우리 Payment 엔티티와 충돌 방지
            IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse =
                    iamportClient.paymentByImpUid(request.getImpUid());

            com.siot.IamportRestClient.response.Payment iamportPayment =
                    iamportResponse.getResponse();

            // 4. 결제 금액 위변조 검증
            BigDecimal requestAmount = new BigDecimal(request.getPaymentAmount());
            if (iamportPayment.getAmount().compareTo(requestAmount) != 0) {
                throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
            }

            // 5. 결제 상태 확인 (paid 여야 성공)
            if (!"paid".equals(iamportPayment.getStatus())) {
                throw new CustomException(ErrorCode.PAYMENT_NOT_PAID);
            }

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("포트원 결제 검증 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_VERIFY_FAILED);
        }

        // 6. 기존 활성 구독 만료 처리
        subscriptionRepository
                .findByPayment_UserAndPlanStatus(user, SubscriptionStatus.ACTIVE)
                .ifPresent(Subscription::expire);

        // 7. 결제 정보 저장
        Payment payment = Payment.create(
                user,
                request.getImpUid(),
                request.getMerchantUid(),
                request.getPaymentAmount(),
                request.getPaymentMethod()
        );
        paymentRepository.save(payment);

        // 8. 구독 정보 저장
        Subscription subscription = Subscription.create(payment, request.getPlanType());
        subscriptionRepository.save(subscription);

        return PaymentDto.ResponseVerify.from(payment, subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto.ResponseSubscription getActiveSubscription(Long userId) {

        // 현재 로그인한 유저의 활성 구독 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Subscription subscription = subscriptionRepository
                .findByPayment_UserAndPlanStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        return PaymentDto.ResponseSubscription.from(subscription);
    }

    @Override
    @Transactional
    public void cancelSubscription(Long userId) {

        // 현재 활성 구독을 취소 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Subscription subscription = subscriptionRepository
                .findByPayment_UserAndPlanStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        subscription.cancel();
    }
}