package kr.co.mapspring.global.jwt;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

@Service
public class AuthCookieService {

    @Value("${auth.cookie.refresh-token-name:refreshToken}")
    private String refreshTokenCookieName;

    @Value("${auth.cookie.path:/}")
    private String cookiePath;

    @Value("${auth.cookie.domain:}")
    private String cookieDomain;

    @Value("${auth.cookie.secure:false}")
    private boolean secure;

    @Value("${auth.cookie.http-only:true}")
    private boolean httpOnly;

    @Value("${auth.cookie.same-site:Lax}")
    private String sameSite;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpirationMs;

    /*
     * Refresh Token을 HttpOnly Cookie로 생성합니다.
     * 프론트 JavaScript에서는 이 쿠키를 읽을 수 없습니다.
     */
    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie
                .from(refreshTokenCookieName, refreshToken)
                .httpOnly(httpOnly)
                .secure(secure)
                .path(cookiePath)
                .sameSite(sameSite)
                .maxAge(Duration.ofMillis(refreshTokenExpirationMs));

        /*
         * 로컬 localhost 환경에서는 domain을 명시하지 않는 것이 안전합니다.
         * domain을 비워두면 host-only cookie로 생성됩니다.
         */
        if (StringUtils.hasText(cookieDomain)) {
            builder.domain(cookieDomain);
        }

        return builder.build();
    }

    /*
     * 로그아웃 시 Refresh Token 쿠키를 즉시 만료시키기 위한 쿠키를 생성합니다.
     */
    public ResponseCookie deleteRefreshTokenCookie() {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie
                .from(refreshTokenCookieName, "")
                .httpOnly(httpOnly)
                .secure(secure)
                .path(cookiePath)
                .sameSite(sameSite)
                .maxAge(0);

        if (StringUtils.hasText(cookieDomain)) {
            builder.domain(cookieDomain);
        }

        return builder.build();
    }

    /*
     * 요청 Cookie에서 Refresh Token 값을 추출합니다.
     * 없으면 INVALID_REFRESH_TOKEN 예외를 발생시킵니다.
     */
    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> refreshTokenCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }
}