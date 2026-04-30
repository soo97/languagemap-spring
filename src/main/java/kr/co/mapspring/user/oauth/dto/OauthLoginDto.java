package kr.co.mapspring.user.oauth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OauthLoginDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestToken {

        /*
         * OAuth 로그인 성공 후 프론트가 받은 1회용 코드입니다.
         * 실제 accessToken/refreshToken이 아니라 Redis 임시 저장값을 찾기 위한 코드입니다.
         */
        @NotBlank
        private String code;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseToken {

        private String accessToken;

        private String refreshToken;

        private Boolean profileRequired;

        public static ResponseToken of(
                String accessToken,
                String refreshToken,
                Boolean profileRequired
        ) {
            return ResponseToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .profileRequired(profileRequired)
                    .build();
        }
    }
}