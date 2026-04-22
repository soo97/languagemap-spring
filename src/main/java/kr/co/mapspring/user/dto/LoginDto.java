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
    public static class Request {

        private String email;

        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Long userId;

        private String email;

        private String name;

        private String role;

        public static Response from(User user) {
            return Response.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .build();
        }
    }
}