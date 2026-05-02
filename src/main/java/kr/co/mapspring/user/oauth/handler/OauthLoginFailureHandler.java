package kr.co.mapspring.user.oauth.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${oauth.google.failure-redirect-url}")
    private String failureRedirectUrl;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        /*
         * 실패 원인을 프론트에서 구분할 수 있게 query parameter로 전달합니다.
         * 운영 환경에서는 상세 메시지를 그대로 노출하지 않는 것이 더 안전합니다.
         */
        String redirectUrl = UriComponentsBuilder
                .fromUriString(failureRedirectUrl)
                .queryParam("error", "OAUTH_LOGIN_FAILED")
                .queryParam("message", exception.getMessage())
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}