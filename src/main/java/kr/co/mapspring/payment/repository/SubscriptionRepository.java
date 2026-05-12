package kr.co.mapspring.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.payment.entity.Payment;
import kr.co.mapspring.payment.entity.Subscription;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.user.entity.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	
    Optional<Subscription> findByPayment_UserAndPlanStatus(User user, SubscriptionStatus status);
    
    Optional<Subscription> findByPayment(Payment payment);
    long countByPlanStatus(SubscriptionStatus planStatus);
}