package kr.co.mapspring.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.co.mapspring.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByImpUid(String impUid);
    
    boolean existsByImpUid(String impUid);
    
    @Query("SELECT p FROM Payment p JOIN FETCH p.user")
    List<Payment> findAllWithUser();
}