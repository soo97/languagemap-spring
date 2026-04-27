package kr.co.mapspring.user.terms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.user.terms.entity.UserTermsAgreement;

public interface UserTermsAgreementRepository extends JpaRepository<UserTermsAgreement, Long> {
}