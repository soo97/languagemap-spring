package kr.co.mapspring.payment.service;

import kr.co.mapspring.payment.dto.PaymentDto;

public interface PaymentService {
    // 결제 검증 및 구독 생성
    PaymentDto.ResponseVerify verifyAndSave(Long userId, PaymentDto.RequestVerify request);

    // 현재 활성 구독 조회
    PaymentDto.ResponseSubscription getActiveSubscription(Long userId);

    // 구독 취소
    void cancelSubscription(Long userId);
}