package kr.co.mapspring.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.support.entity.CounselImg;

public interface CounselImgRepository extends JpaRepository<CounselImg, Long> {
	 
    // 특정 문의의 이미지 목록 조회
    List<CounselImg> findByCounselCounselId(Long counselId);
 
    // 특정 문의의 이미지 전체 삭제 (문의 삭제 시 사용)
    void deleteByCounselCounselId(Long counselId);
}