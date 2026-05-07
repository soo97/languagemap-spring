package kr.co.mapspring.user.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OauthLoginDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "OAuth 토큰 교환 요청 DTO")
    public static class RequestToken {

        /*
         * OAuth 로그인 성공 후 프론트가 받은 1회용 code입니다.
         * 실제 accessToken/refreshToken이 아니라 Redis 임시 저장값을 찾기 위한 code입니다.
         */
        @NotBlank(message = "OAuth 로그인 code는 필수 입력 값입니다.")
        @Schema(description = "OAuth 로그인 성공 후 발급된 1회용 code", example = "3f1b8f1e-4f4a-4c25-9c91-123456789abc")
        private String code;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "OAuth 토큰 교환 응답 DTO")
    public static class ResponseToken {

        /*
         * 프론트가 Authorization Header에 사용할 Access Token입니다.
         */
        @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
        private String accessToken;

        /*
         * 소셜 회원의 추가 프로필 입력 필요 여부입니다.
         */
        @Schema(description = "추가 프로필 입력 필요 여부", example = "true")
        private Boolean profileRequired;

        public static ResponseToken from(TokenResult tokenResult) {
            return ResponseToken.builder()
                    .accessToken(tokenResult.getAccessToken())
                    .profileRequired(tokenResult.getProfileRequired())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResult {

        /*
         * 내부 처리/Redis 저장용 DTO입니다.
         * refreshToken이 포함되지만 API 응답 DTO로 직접 반환하지 않습니다.
         */
        private String accessToken;

        private String refreshToken;

        private Boolean profileRequired;

        public static TokenResult of(
                String accessToken,
                String refreshToken,
                Boolean profileRequired
        ) {
            return TokenResult.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .profileRequired(profileRequired)
                    .build();
        }
    }
}