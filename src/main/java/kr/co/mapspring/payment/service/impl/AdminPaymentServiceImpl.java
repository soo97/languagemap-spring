package kr.co.mapspring.payment.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.payment.dto.AdminPaymentDto;
import kr.co.mapspring.payment.entity.Payment;
import kr.co.mapspring.payment.enums.PaymentStatus;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.payment.repository.PaymentRepository;
import kr.co.mapspring.payment.repository.SubscriptionRepository;
import kr.co.mapspring.payment.service.AdminPaymentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminPaymentDto.ResponseList> getPaymentList() {
        return paymentRepository.findAllWithUser().stream()
                .map(payment -> {
                    var subscription = subscriptionRepository
                            .findByPayment(payment)
                            .orElse(null);
                    return AdminPaymentDto.ResponseList.from(payment, subscription);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminPaymentDto.ResponseStats getPaymentStats() {
        List<Payment> allPayments = paymentRepository.findAllWithUser();

        long totalCount = allPayments.size();
        long successCount = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .count();
        long totalAmount = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.SUCCESS)
                .mapToLong(p -> p.getPaymentAmount() != null ? p.getPaymentAmount() : 0)
                .sum();
        long activeSubscriptionCount = subscriptionRepository.countByPlanStatus(SubscriptionStatus.ACTIVE);

        return AdminPaymentDto.ResponseStats.builder()
                .totalPaymentCount(totalCount)
                .successPaymentCount(successCount)
                .activeSubscriptionCount(activeSubscriptionCount)
                .totalAmount(totalAmount)
                .build();
    }
}