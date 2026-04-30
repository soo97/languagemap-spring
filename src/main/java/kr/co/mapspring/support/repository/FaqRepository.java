package kr.co.mapspring.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.support.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long> {
	 
    // 전체 목록 조회
    List<Faq> findAll();
}