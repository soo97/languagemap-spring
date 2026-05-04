package kr.co.mapspring.user.oauth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.global.jwt.RefreshTokenService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.oauth.dto.OauthUserDto;
import kr.co.mapspring.user.oauth.service.OauthLoginCodeService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OauthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final OauthLoginCodeService oauthLoginCodeService;

    @Value("${oauth.google.success-redirect-url}")
    private String successRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        /*
         * OauthUserService에서 반환한 principal을 가져옵니다.
         */
        OauthUserDto principal = (OauthUserDto) authentication.getPrincipal();
        User user = principal.getUser();

        /*
         * 기존 JWT 발급 로직을 재사용합니다.
         */
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        /*
         * Refresh Token은 기존 Redis 저장 구조를 유지합니다.
         */
        refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);

        /*
         * 실제 토큰은 URL에 싣지 않고 Redis에 1회용 code로 임시 저장합니다.
         */
        boolean profileRequired = user.isProfileIncomplete();

        String code = oauthLoginCodeService.saveToken(
                accessToken,
                refreshToken,
                profileRequired
        );

        /*
         * 프론트에는 실제 토큰 대신 1회용 code만 전달합니다.
         */
        String redirectUrl = UriComponentsBuilder
                .fromUriString(successRedirectUrl)
                .queryParam("code", code)
                .queryParam("profileRequired", profileRequired)
                .build()
                .toUriString();

        clearAuthenticationAttributes(request);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}