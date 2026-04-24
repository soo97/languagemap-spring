package kr.co.mapspring.user.terms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.user.terms.entity.Terms;
import kr.co.mapspring.user.terms.enums.TermsType;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    // 활성화된 특정 타입의 약관 1개 조회
	Optional<Terms> findByTermTypeAndActiveTrue(TermsType termType);
    // 현재 활성화된 약관 전체 조회
    List<Terms> findAllByActiveTrue();
}