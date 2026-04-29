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
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OauthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${oauth.google.success-redirect-url}")
    private String successRedirectUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        /*
         * 1. OauthUserService에서 반환한 principal을 가져옵니다.
         */
        OauthUserDto principal = (OauthUserDto) authentication.getPrincipal();
        User user = principal.getUser();

        /*
         * 2. 기존 JWT 발급 로직을 재사용합니다.
         */
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        /*
         * 3. 기존 Redis Refresh Token 저장 로직을 재사용합니다.
         */
        refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken);

        /*
         * 4. 소셜 회원은 프로필 필드가 null일 수 있으므로
         * 프론트가 추가 정보 입력 필요 여부를 판단할 수 있게 내려줍니다.
         */
        boolean profileRequired = user.isProfileIncomplete();

        /*
         * 5. 프론트 성공 페이지로 redirect합니다.
         * 1차 구현에서는 Query Parameter 방식으로 토큰을 전달합니다.
         */
        String redirectUrl = UriComponentsBuilder
                .fromUriString(successRedirectUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("profileRequired", profileRequired)
                .build()
                .toUriString();

        /*
         * 6. OAuth 인증 과정에서 사용한 임시 인증 정보를 정리합니다.
         */
        clearAuthenticationAttributes(request);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}