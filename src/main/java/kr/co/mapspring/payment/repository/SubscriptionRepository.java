package kr.co.mapspring.payment.repository;

import kr.co.mapspring.payment.entity.Subscription;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByPayment_UserAndPlanStatus(User user, SubscriptionStatus status);
}