package kr.co.mapspring.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.social.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AdminSocialDto {

    @Getter
    @Builder
    @Schema(description = "관리자 신고 응답 DTO")
    public static class ResponseReport {

        @Schema(description = "신고 ID", example = "1")
        private Long reportId;

        @Schema(description = "신고자 ID", example = "1")
        private Long reporterId;

        @Schema(description = "신고 대상 사용자 ID", example = "2")
        private Long reportedUserId;

        @Schema(description = "신고 사유", example = "욕설")
        private String reason;

        @Schema(description = "신고 상태", example = "PENDING")
        private ReportStatus status;

        @Schema(description = "생성 시간")
        private LocalDateTime createdAt;

        @Schema(description = "처리 시간")
        private LocalDateTime processedAt;

        @Schema(description = "관리자 메모")
        private String adminMemo;

        public static ResponseReport from(UserReport userReport) {
            return ResponseReport.builder()
                    .reportId(userReport.getReportId())
                    .reporterId(userReport.getReporter().getUserId())
                    .reportedUserId(userReport.getReportedUser().getUserId())
                    .reason(userReport.getReason())
                    .status(userReport.getStatus())
                    .createdAt(userReport.getCreatedAt())
                    .processedAt(userReport.getProcessedAt())
                    .adminMemo(userReport.getAdminMemo())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자 신고 상태 변경 요청 DTO")
    public static class RequestUpdateReportStatus {

        @Schema(description = "변경할 상태", example = "RESOLVED")
        private ReportStatus status;

        @Schema(description = "관리자 메모", example = "확인 후 처리 완료")
        private String adminMemo;
    }

    @Getter
    @Builder
    @Schema(description = "관리자 친구 관계 이력 응답 DTO")
    public static class ResponseFriendshipHistory {

        @Schema(description = "친구 관계 ID", example = "1")
        private Long friendshipId;

        @Schema(description = "요청 보낸 사용자 ID", example = "1")
        private Long requesterId;

        @Schema(description = "요청 받은 사용자 ID", example = "2")
        private Long addresseeId;

        @Schema(description = "친구 상태", example = "BLOCKED")
        private FriendshipStatus status;

        @Schema(description = "요청 시간")
        private LocalDateTime requestedAt;

        @Schema(description = "응답 시간")
        private LocalDateTime respondedAt;

        public static ResponseFriendshipHistory from(Friendship friendship) {
            return ResponseFriendshipHistory.builder()
                    .friendshipId(friendship.getFriendshipId())
                    .requesterId(friendship.getRequester().getUserId())
                    .addresseeId(friendship.getAddressee().getUserId())
                    .status(friendship.getStatus())
                    .requestedAt(friendship.getRequestedAt())
                    .respondedAt(friendship.getRespondedAt())
                    .build();
        }
    }
}
