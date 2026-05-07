package kr.co.mapspring.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

        /*
         * HttpOnly Cookie 전환 후에는 클라이언트가 직접 보내지 않습니다.
         * Controller가 Cookie에서 꺼낸 refreshToken으로 이 DTO를 생성합니다.
         */
        @NotBlank(message = "Refresh Token은 필수 입력 값입니다.")
        @Schema(hidden = true)
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

        /*
         * 새 Refresh Token은 HttpOnly Cookie로 내려보냅니다.
         * JSON 응답에는 포함하지 않습니다.
         */
        @JsonIgnore
        @Schema(hidden = true)
        private String refreshToken;

        public static ResponseReissue of(String accessToken, String refreshToken) {
            return ResponseReissue.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "로그아웃 요청 DTO")
    public static class RequestLogout {

        /*
         * HttpOnly Cookie 전환 후에는 클라이언트가 직접 보내지 않습니다.
         * Controller가 Cookie에서 꺼낸 refreshToken으로 이 DTO를 생성합니다.
         */
        @NotBlank(message = "Refresh Token은 필수 입력 값입니다.")
        @Schema(hidden = true)
        private String refreshToken;
    }
}