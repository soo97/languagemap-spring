package kr.co.mapspring.social.service;

import kr.co.mapspring.social.dto.AdminSocialDto;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.social.enums.ReportStatus;
import kr.co.mapspring.social.repository.FriendshipRepository;
import kr.co.mapspring.social.repository.UserReportRepository;
import kr.co.mapspring.social.service.impl.AdminSocialServiceImpl;
import kr.co.mapspring.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminSocialServiceTest {

    @Mock
    private UserReportRepository userReportRepository;

    @Mock
    private FriendshipRepository friendshipRepository;

    @InjectMocks
    private AdminSocialServiceImpl adminSocialService;

    @Test
    @DisplayName("관리자는 신고 목록을 조회한다")
    void 관리자는_신고_목록을_조회한다() {

        User reporter = mock(User.class);
        User reportedUser = mock(User.class);

        given(reporter.getUserId()).willReturn(1L);
        given(reportedUser.getUserId()).willReturn(2L);

        UserReport report1 = UserReport.of(
                1L,
                reporter,
                reportedUser,
                "욕설 신고",
                ReportStatus.PENDING
        );

        UserReport report2 = UserReport.of(
                2L,
                reporter,
                reportedUser,
                "부적절한 프로필",
                ReportStatus.PENDING
        );

        given(userReportRepository.findAllByOrderByCreatedAtDesc())
                .willReturn(List.of(report1, report2));

        List<AdminSocialDto.ResponseReport> result = adminSocialService.getReports();

        verify(userReportRepository).findAllByOrderByCreatedAtDesc();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getReportId());
        assertEquals(1L, result.get(0).getReporterId());
        assertEquals(2L, result.get(0).getReportedUserId());
        assertEquals("욕설 신고", result.get(0).getReason());
        assertEquals(ReportStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    @DisplayName("관리자는 신고 상세를 조회한다")
    void 관리자는_신고_상세를_조회한다() {

        Long reportId = 1L;

        User reporter = mock(User.class);
        User reportedUser = mock(User.class);

        given(reporter.getUserId()).willReturn(1L);
        given(reportedUser.getUserId()).willReturn(2L);

        UserReport report = UserReport.of(
                reportId,
                reporter,
                reportedUser,
                "욕설 신고",
                ReportStatus.PENDING
        );

        given(userReportRepository.findById(reportId))
                .willReturn(Optional.of(report));

        AdminSocialDto.ResponseReport result = adminSocialService.getReport(reportId);

        verify(userReportRepository).findById(reportId);

        assertEquals(reportId, result.getReportId());
        assertEquals(1L, result.getReporterId());
        assertEquals(2L, result.getReportedUserId());
        assertEquals("욕설 신고", result.getReason());
        assertEquals(ReportStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 신고 상세 조회 시 예외가 발생한다")
    void 존재하지_않는_신고_상세_조회시_예외가_발생한다() {

        Long reportId = 999L;

        given(userReportRepository.findById(reportId))
                .willReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> adminSocialService.getReport(reportId));

        verify(userReportRepository).findById(reportId);
    }

    @Test
    @DisplayName("관리자는 신고를 처리 완료 상태로 변경한다")
    void 관리자는_신고를_처리_완료_상태로_변경한다() {

        Long reportId = 1L;
        String adminMemo = "신고 내용 확인 후 처리 완료";

        User reporter = mock(User.class);
        User reportedUser = mock(User.class);

        UserReport report = UserReport.of(
                reportId,
                reporter,
                reportedUser,
                "욕설 신고",
                ReportStatus.PENDING
        );

        AdminSocialDto.RequestUpdateReportStatus request =
                new AdminSocialDto.RequestUpdateReportStatus(
                        ReportStatus.RESOLVED,
                        adminMemo
                );

        given(userReportRepository.findById(reportId))
                .willReturn(Optional.of(report));

        adminSocialService.updateReportStatus(reportId, request);

        verify(userReportRepository).findById(reportId);

        assertEquals(ReportStatus.RESOLVED, report.getStatus());
        assertEquals(adminMemo, report.getAdminMemo());
        assertNotNull(report.getProcessedAt());
    }

    @Test
    @DisplayName("관리자는 신고를 반려 상태로 변경한다")
    void 관리자는_신고를_반려_상태로_변경한다() {

        Long reportId = 1L;
        String adminMemo = "신고 사유가 부족하여 반려";

        User reporter = mock(User.class);
        User reportedUser = mock(User.class);

        UserReport report = UserReport.of(
                reportId,
                reporter,
                reportedUser,
                "욕설 신고",
                ReportStatus.PENDING
        );

        AdminSocialDto.RequestUpdateReportStatus request =
                new AdminSocialDto.RequestUpdateReportStatus(
                        ReportStatus.REJECTED,
                        adminMemo
                );

        given(userReportRepository.findById(reportId))
                .willReturn(Optional.of(report));

        adminSocialService.updateReportStatus(reportId, request);

        verify(userReportRepository).findById(reportId);

        assertEquals(ReportStatus.REJECTED, report.getStatus());
        assertEquals(adminMemo, report.getAdminMemo());
        assertNotNull(report.getProcessedAt());
    }

    @Test
    @DisplayName("관리자는 차단 이력을 조회한다")
    void 관리자는_차단_이력을_조회한다() {

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(requester.getUserId()).willReturn(1L);
        given(addressee.getUserId()).willReturn(2L);

        Friendship friendship1 = Friendship.of(
                1L,
                requester,
                addressee,
                FriendshipStatus.BLOCKED
        );

        Friendship friendship2 = Friendship.of(
                2L,
                requester,
                addressee,
                FriendshipStatus.BLOCKED
        );

        given(friendshipRepository.findAllByStatusOrderByRespondedAtDesc(FriendshipStatus.BLOCKED))
                .willReturn(List.of(friendship1, friendship2));

        List<AdminSocialDto.ResponseFriendshipHistory> result =
                adminSocialService.getBlockedFriendships();

        verify(friendshipRepository).findAllByStatusOrderByRespondedAtDesc(FriendshipStatus.BLOCKED);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getFriendshipId());
        assertEquals(1L, result.get(0).getRequesterId());
        assertEquals(2L, result.get(0).getAddresseeId());
        assertEquals(FriendshipStatus.BLOCKED, result.get(0).getStatus());
    }

    @Test
    @DisplayName("관리자는 거절 이력을 조회한다")
    void 관리자는_거절_이력을_조회한다() {

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(requester.getUserId()).willReturn(1L);
        given(addressee.getUserId()).willReturn(2L);

        Friendship friendship1 = Friendship.of(
                1L,
                requester,
                addressee,
                FriendshipStatus.REJECTED
        );

        Friendship friendship2 = Friendship.of(
                2L,
                requester,
                addressee,
                FriendshipStatus.REJECTED
        );

        given(friendshipRepository.findAllByStatusOrderByRespondedAtDesc(FriendshipStatus.REJECTED))
                .willReturn(List.of(friendship1, friendship2));

        List<AdminSocialDto.ResponseFriendshipHistory> result =
                adminSocialService.getRejectedFriendships();

        verify(friendshipRepository).findAllByStatusOrderByRespondedAtDesc(FriendshipStatus.REJECTED);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getFriendshipId());
        assertEquals(1L, result.get(0).getRequesterId());
        assertEquals(2L, result.get(0).getAddresseeId());
        assertEquals(FriendshipStatus.REJECTED, result.get(0).getStatus());
    }
}