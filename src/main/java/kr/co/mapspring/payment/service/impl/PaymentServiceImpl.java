package kr.co.mapspring.payment.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        // 3. 포트원 REST API 직접 호출로 결제 검증
        // iamport 라이브러리 대신 WebClient로 직접 호출 (라이브러리 버전 호환 문제 해결)
//        try {
//            WebClient webClient = WebClient.create();
//            ObjectMapper mapper = new ObjectMapper();
//
//            // 3-1. 포트원 액세스 토큰 발급
//            log.info("포트원 토큰 발급 시도...");
//            String tokenResponse = webClient.post()
//                    .uri("https://api.iamport.kr/users/getToken")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue("{\"imp_key\":\"" + impKey + "\",\"imp_secret\":\"" + impSecret + "\"}")
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//            JsonNode tokenNode = mapper.readTree(tokenResponse);
//            String accessToken = tokenNode.path("response").path("access_token").asText();
//            log.info("포트원 토큰 발급 성공");
//
//            // 3-2. imp_uid로 결제 정보 조회
//         // << 변경: 테스트 결제 조회용 URL
//            String paymentResponse = webClient.get()
//                    .uri("https://api.iamport.kr/payments/" + request.getImpUid())
//                    .header("Authorization", accessToken)
//                    .header("X-ImpTokenHeader", accessToken)  // << 추가
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//            JsonNode paymentNode = mapper.readTree(paymentResponse);
//            JsonNode paymentData = paymentNode.path("response");
//
//            log.info("포트원 결제 상태: {}", paymentData.path("status").asText());
//            log.info("포트원 결제 금액: {}", paymentData.path("amount").asInt());
//
//            // 4. 결제 금액 위변조 검증
//            // 프론트에서 보낸 금액과 포트원 실제 결제 금액 비교
//            int paidAmount = paymentData.path("amount").asInt();
//            if (paidAmount != request.getPaymentAmount()) {
//                throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
//            }
//
//            // 5. 결제 상태 확인 (paid 여야 성공)
//            String status = paymentData.path("status").asText();
//            if (!"paid".equals(status)) {
//                throw new CustomException(ErrorCode.PAYMENT_NOT_PAID);
//            }
//
//        } catch (CustomException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("포트원 결제 검증 실패 상세: {}", e.getMessage(), e);
//            throw new CustomException(ErrorCode.PAYMENT_VERIFY_FAILED);
//        }

        // 6. 기존 활성 구독 만료 처리
        // 새로운 구독 전 기존 구독이 있으면 만료 처리
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