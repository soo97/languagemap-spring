package kr.co.mapspring.social.service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.enums.ReportStatus;
import kr.co.mapspring.social.repository.UserReportRepository;
import kr.co.mapspring.social.service.impl.UserReportServiceImpl;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserReportServiceTest {

    @Mock
    private UserReportRepository userReportRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserReportServiceImpl userReportService;

    @Test
    @DisplayName("사용자를 정상적으로 신고한다")
    void 사용자를_정상적으로_신고한다() {

        Long reporterId = 1L;
        Long reportedUserId = 2L;
        String reason = "욕설 및 부적절한 행동";

        User reporter = mock(User.class);
        User reportedUser = mock(User.class);

        given(userRepository.findById(reporterId))
                .willReturn(Optional.of(reporter));

        given(userRepository.findById(reportedUserId))
                .willReturn(Optional.of(reportedUser));

        userReportService.createReport(reporterId, reportedUserId, reason);

        ArgumentCaptor<UserReport> captor = ArgumentCaptor.forClass(UserReport.class);
        verify(userReportRepository).save(captor.capture());

        UserReport savedReport = captor.getValue();

        assertEquals(reporter, savedReport.getReporter());
        assertEquals(reportedUser, savedReport.getReportedUser());
        assertEquals(reason, savedReport.getReason());
        assertEquals(ReportStatus.PENDING, savedReport.getStatus());
    }

    @Test
    @DisplayName("자기 자신을 신고할 수 없다")
    void 자기_자신을_신고할_수_없다() {

        Long userId = 1L;

        assertThrows(CustomException.class,
                () -> userReportService.createReport(userId, userId, "신고 사유"));

        verify(userRepository, never()).findById(any(Long.class));
        verify(userReportRepository, never()).save(any(UserReport.class));
    }

    @Test
    @DisplayName("신고 사유가 비어 있으면 신고할 수 없다")
    void 신고_사유가_비어_있으면_신고할_수_없다() {

        Long reporterId = 1L;
        Long reportedUserId = 2L;

        assertThrows(CustomException.class,
                () -> userReportService.createReport(reporterId, reportedUserId, " "));

        verify(userRepository, never()).findById(any(Long.class));
        verify(userReportRepository, never()).save(any(UserReport.class));
    }
}
