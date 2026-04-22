package kr.co.mapspring.user.dto;

import kr.co.mapspring.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LoginDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestLogin {

        private String email;

        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseLogin {

        private Long userId;

        private String email;

        private String name;

        private String role;

        public static ResponseLogin from(User user) {
            return ResponseLogin.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .build();
        }
    }
}