package kr.co.mapspring.social.service;

import kr.co.mapspring.social.dto.AdminSocialDto;

import java.util.List;

public interface AdminSocialService {

    /**
     * 신고 목록 조회
     *
     * [설명]
     * - 관리자가 전체 사용자 신고 목록을 조회한다.
     * - 최신 신고가 먼저 조회된다.
     *
     * @return 신고 목록
     */
    List<AdminSocialDto.ResponseReport> getReports();

    /**
     * 신고 상세 조회
     *
     * [설명]
     * - 관리자가 특정 신고 내용을 상세 조회한다.
     *
     * [검증]
     * - reportId에 해당하는 신고가 존재해야 한다.
     *
     * @param reportId 신고 ID
     * @return 신고 상세 정보
     */
    AdminSocialDto.ResponseReport getReport(Long reportId);

    /**
     * 신고 처리 상태 변경
     *
     * [설명]
     * - 관리자가 신고 상태를 처리 완료 또는 반려로 변경한다.
     * - 처리 시 관리자 메모를 함께 저장한다.
     *
     * [검증]
     * - reportId에 해당하는 신고가 존재해야 한다.
     *
     * @param reportId 신고 ID
     * @param request 신고 상태 변경 요청 DTO
     */
    void updateReportStatus(Long reportId, AdminSocialDto.RequestUpdateReportStatus request);

    /**
     * 차단 이력 조회
     *
     * [설명]
     * - 관리자가 전체 친구 관계 중 차단 상태의 이력을 조회한다.
     *
     * @return 차단 이력 목록
     */
    List<AdminSocialDto.ResponseFriendshipHistory> getBlockedFriendships();

    /**
     * 거절 이력 조회
     *
     * [설명]
     * - 관리자가 전체 친구 관계 중 거절 상태의 이력을 조회한다.
     *
     * @return 거절 이력 목록
     */
    List<AdminSocialDto.ResponseFriendshipHistory> getRejectedFriendships();
}


