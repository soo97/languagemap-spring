package kr.co.mapspring.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "토큰 재발급 요청 DTO")
    public static class RequestReissue {

        @NotBlank(message = "Refresh Token은 필수 입력 값입니다.")
        @Schema(description = "JWT Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9...")
        private String refreshToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "토큰 재발급 응답 DTO")
    public static class ResponseReissue {

        @Schema(description = "새로 발급된 JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
        private String accessToken;

        @Schema(description = "새로 발급된 JWT Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9...")
        private String refreshToken;

        public static ResponseReissue of(String accessToken, String refreshToken) {
            return ResponseReissue.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}