package kr.co.mapspring.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.support.entity.NoticeImg;

public interface NoticeImgRepository extends JpaRepository<NoticeImg, Long> {
	 
    // 특정 공지의 이미지 목록 조회
    List<NoticeImg> findByNoticeNoticeId(Long noticeId);
 
    // 특정 공지의 이미지 전체 삭제 (공지 삭제 시 사용)
    void deleteByNoticeNoticeId(Long noticeId);
}
 
