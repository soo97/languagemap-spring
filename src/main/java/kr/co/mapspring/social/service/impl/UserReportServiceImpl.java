package kr.co.mapspring.social.service.impl;

import kr.co.mapspring.global.exception.social.InvalidReportReasonException;
import kr.co.mapspring.global.exception.social.InvalidReportUserException;
import kr.co.mapspring.global.exception.social.SelfReportNotAllowedException;
import kr.co.mapspring.global.exception.social.UserNotFoundForSocialException;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.repository.UserReportRepository;
import kr.co.mapspring.social.service.UserReportService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReportServiceImpl implements UserReportService {

    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;

    @Override
    @Transactional
    public void createReport(Long reporterId, Long reportedUserId, String reason) {

        if (reporterId == null || reportedUserId == null) {
            throw new InvalidReportUserException();
        }

        if (reporterId.equals(reportedUserId)) {
            throw new SelfReportNotAllowedException();
        }

        if (reason == null || reason.isBlank()) {
            throw new InvalidReportReasonException();
        }

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(UserNotFoundForSocialException::new);

        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(UserNotFoundForSocialException::new);

        UserReport userReport = UserReport.create(
                reporter,
                reportedUser,
                reason
        );

        userReportRepository.save(userReport);
    }

    @Override
    public List<UserReport> getReportHistory(Long userId) {

        if (userId == null) {
            throw new InvalidReportUserException();
        }

        return userReportRepository.findByReporter_UserId(userId);
    }
}
