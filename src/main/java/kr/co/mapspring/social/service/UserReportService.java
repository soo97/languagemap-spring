package kr.co.mapspring.social.service;

import kr.co.mapspring.social.entity.UserReport;

import java.util.List;

public interface UserReportService {

    /**
     * 사용자 신고
     *
     * [설명]
     * - 특정 사용자를 신고한다.
     *
     * [검증]
     * - 신고자 ID와 신고 대상 사용자 ID는 필수 값이다.
     * - 자기 자신을 신고할 수 없다.
     * - 신고 사유는 필수 값이다.
     * - 신고자와 신고 대상 사용자가 존재해야 한다.
     *
     * @param reporterId 신고자 ID
     * @param reportedUserId 신고 대상 사용자 ID
     * @param reason 신고 사유
     */
    void createReport(Long reporterId, Long reportedUserId, String reason);

    /**
     * 신고 이력 조회
     *
     * [설명]
     * - 특정 사용자가 신고한 이력을 조회한다.
     *
     * [검증]
     * - userId는 필수 값이다.
     *
     * @param userId 신고 이력을 조회할 사용자 ID
     * @return 사용자가 신고한 이력 목록
     */
    List<UserReport> getReportHistory(Long userId);
}
