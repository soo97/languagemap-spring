package kr.co.mapspring.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.user.entity.User;
import lombok.Builder;
import lombok.Getter;

public class UserDto {

    @Getter
    @Builder
    @Schema(description = "내 정보 응답 DTO")
    public static class ResponseMe {

        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "이메일", example = "test@naver.com")
        private String email;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        @Schema(description = "역할", example = "USER")
        private String role;

        @Schema(description = "상태", example = "ACTIVE")
        private String status;

        public static ResponseMe from(User user) {
            return ResponseMe.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .status(user.getStatus().name())
                    .build();
        }
    }
}