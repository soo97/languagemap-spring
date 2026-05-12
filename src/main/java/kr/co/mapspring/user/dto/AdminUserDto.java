package kr.co.mapspring.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminUserDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "회원 목록 응답 DTO")
    public static class ResponseList {
        private Long userId;
        private String email;
        private String name;
        private String phoneNumber;
        private String role;
        private String status;
        private LocalDateTime createdAt;

        public static ResponseList from(User user) {
            return ResponseList.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().name())
                    .status(user.getStatus().name())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "회원 상세 응답 DTO")
    public static class ResponseDetail {
        private Long userId;
        private String email;
        private String name;
        private LocalDate birthDate;
        private String address;
        private String phoneNumber;
        private String role;
        private String status;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ResponseDetail from(User user) {
            return ResponseDetail.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .birthDate(user.getBirthDate())
                    .address(user.getAddress())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().name())
                    .status(user.getStatus().name())
                    .lastLoginAt(user.getLastLoginAt())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 상태 변경 요청 DTO")
    public static class RequestUpdateStatus {
        @Schema(description = "변경할 상태", example = "SUSPENDED")
        private UserStatus status;
    }
}