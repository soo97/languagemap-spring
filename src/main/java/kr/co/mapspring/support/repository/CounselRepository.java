package kr.co.mapspring.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.support.entity.Counsel;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
	 
    // 특정 사용자의 문의 목록 조회 (최신순)
    List<Counsel> findByUserUserIdOrderByCounselDateDesc(Long userId);
}
