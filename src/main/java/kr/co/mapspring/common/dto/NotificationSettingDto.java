package kr.co.mapspring.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationSettingDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "알림 설정 변경 요청 DTO")
    public static class Request {
 
        @Schema(description = "이메일 알림 수신 여부", example = "true")
        private boolean emailNotification;
    }
 
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "알림 설정 응답 DTO")
    public static class Response {
 
        @Schema(description = "이메일 알림 수신 여부", example = "true")
        private boolean emailNotification;
 
        public static Response from(boolean emailNotification) {
            return Response.builder()
                    .emailNotification(emailNotification)
                    .build();
        }
    }
}
