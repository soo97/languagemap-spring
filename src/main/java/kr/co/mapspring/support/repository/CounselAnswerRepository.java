package kr.co.mapspring.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.support.entity.CounselAnswer;

public interface CounselAnswerRepository extends JpaRepository<CounselAnswer, Long> {
	 
    // 특정 문의의 답변 목록 조회
    List<CounselAnswer> findByCounselCounselId(Long counselId);
 
    // 특정 문의의 답변 전체 삭제 (문의 삭제 시 사용)
    void deleteByCounselCounselId(Long counselId);
}