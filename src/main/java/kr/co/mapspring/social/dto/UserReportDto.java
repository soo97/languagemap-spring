package kr.co.mapspring.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
}
