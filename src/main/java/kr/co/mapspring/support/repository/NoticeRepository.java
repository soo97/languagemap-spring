package kr.co.mapspring.support.repository;

import kr.co.mapspring.support.entity.Notice;
import kr.co.mapspring.support.entity.NoticeKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	 
    // 전체 목록 조회 (최신순)
    List<Notice> findAllByOrderByNoticeDateDesc();
 
    // 유형별 목록 조회 (최신순)
    List<Notice> findByNoticeKindOrderByNoticeDateDesc(NoticeKind noticeKind);
}
