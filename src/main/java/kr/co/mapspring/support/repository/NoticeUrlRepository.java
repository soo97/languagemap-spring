package kr.co.mapspring.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.support.entity.NoticeUrl;

public interface NoticeUrlRepository extends JpaRepository<NoticeUrl, Long> {
	 
    // 특정 공지의 URL 목록 조회
    List<NoticeUrl> findByNoticeNoticeId(Long noticeId);
 
    // 특정 공지의 URL 전체 삭제 (공지 삭제 시 사용)
    void deleteByNoticeNoticeId(Long noticeId);
}