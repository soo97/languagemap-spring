package kr.co.mapspring.payment.service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.payment.repository.SubscriptionRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubscriptionValidator {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    /**
     * 유저의 활성 구독 여부를 검증합니다.
     * 구독이 없거나 만료된 경우 예외를 발생시킵니다.
     * 유료 기능 API 시작 시 호출하세요.
     *
     * 사용 예시:
     * subscriptionValidator.validate(userId);
     */
    @Transactional(readOnly = true)
    public void validate(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        subscriptionRepository
                .findByPayment_UserAndPlanStatus(user, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.AI_COACHING_ACCESS_DENIED));
    }

    /**
     * 구독 여부만 boolean으로 반환합니다.
     * 예외 없이 구독 여부만 확인할 때 사용
     *
     * 사용 예시:
     * boolean isPremium = subscriptionValidator.isPremium(userId);
     */
    @Transactional(readOnly = true)
    public boolean isPremium(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return subscriptionRepository
                .findByPayment_UserAndPlanStatus(user, SubscriptionStatus.ACTIVE)
                .isPresent();
    }
}