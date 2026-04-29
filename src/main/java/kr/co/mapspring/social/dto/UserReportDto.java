package kr.co.mapspring.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.social.entity.UserReport;
import kr.co.mapspring.social.enums.ReportStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

public class UserReportDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "사용자 신고 요청 DTO")
    public static class RequestCreateReport {

        @Schema(description = "신고자 ID", example = "1")
        private Long reporterId;

        @Schema(description = "신고 대상 사용자 ID", example = "2")
        private Long reportedUserId;

        @Schema(description = "신고 사유", example = "욕설 및 부적절한 행동")
        private String reason;
    }

    @Getter
    @Builder
    @Schema(description = "신고 이력 응답 DTO")
    public static class ResponseReportHistory {

        @Schema(description = "신고 ID", example = "1")
        private Long reportId;

        @Schema(description = "신고 대상 사용자 ID", example = "2")
        private Long reportedUserId;

        @Schema(description = "신고 사유", example = "욕설")
        private String reason;

        @Schema(description = "신고 상태", example = "PENDING")
        private ReportStatus status;

        public static ResponseReportHistory from(UserReport userReport) {
            return ResponseReportHistory.builder()
                    .reportId(userReport.getReportId())
                    .reportedUserId(userReport.getReportedUser().getUserId())
                    .reason(userReport.getReason())
                    .status(userReport.getStatus())
                    .build();
        }
    }
}
