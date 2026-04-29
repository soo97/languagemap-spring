package kr.co.mapspring.social.service;

import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.social.enums.ReportStatus;
import kr.co.mapspring.social.repository.FriendshipRepository;
import kr.co.mapspring.social.repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSocialServiceImpl implements AdminSocialService {

    private final UserReportRepository userReportRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public List<AdminSocialDto.ResponseReport> getReports() {
        return userReportRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(AdminSocialDto.ResponseReport::from)
                .toList();
    }

    @Override
    public AdminSocialDto.ResponseReport getReport(Long reportId) {
        UserReport userReport = userReportRepository.findById(reportId)
                .orElseThrow(UserReportNotFoundException::new);

        return AdminSocialDto.ResponseReport.from(userReport);
    }

    @Override
    @Transactional
    public void updateReportStatus(Long reportId, AdminSocialDto.RequestUpdateReportStatus request) {
        UserReport userReport = userReportRepository.findById(reportId)
                .orElseThrow(UserReportNotFoundException::new);

        if (request.getStatus() == ReportStatus.RESOLVED) {
            userReport.resolve(request.getAdminMemo());
            return;
        }

        if (request.getStatus() == ReportStatus.REJECTED) {
            userReport.reject(request.getAdminMemo());
        }
    }

    @Override
    public List<AdminSocialDto.ResponseFriendshipHistory> getBlockedFriendships() {
        return getFriendshipsByStatus(FriendshipStatus.BLOCKED);
    }

    @Override
    public List<AdminSocialDto.ResponseFriendshipHistory> getRejectedFriendships() {
        return getFriendshipsByStatus(FriendshipStatus.REJECTED);
    }

    private List<AdminSocialDto.ResponseFriendshipHistory> getFriendshipsByStatus(
            FriendshipStatus status
    ) {
        List<Friendship> friendships =
                friendshipRepository.findAllByStatusOrderByRespondedAtDesc(status);

        return friendships.stream()
                .map(AdminSocialDto.ResponseFriendshipHistory::from)
                .toList();
    }
}
