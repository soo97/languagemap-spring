package kr.co.mapspring.social.service.impl;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.repository.UserReportRepository;
import kr.co.mapspring.social.service.UserReportService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CustomException(ErrorCode.BAD_REQUEST, "사용자 ID는 필수입니다.");
        }

        if (reporterId.equals(reportedUserId)) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "자기 자신을 신고할 수 없습니다.");
        }

        if (reason == null || reason.isBlank()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "신고 사유는 필수입니다.");
        }

        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "신고자를 찾을 수 없습니다."));

        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "신고 대상 사용자를 찾을 수 없습니다."));

        UserReport userReport = UserReport.create(
                reporter,
                reportedUser,
                reason
        );

        userReportRepository.save(userReport);
    }
}
