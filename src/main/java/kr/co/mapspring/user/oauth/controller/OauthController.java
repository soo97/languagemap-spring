package kr.co.mapspring.user.oauth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.global.jwt.AuthCookieService;
import kr.co.mapspring.user.oauth.controller.docs.OauthControllerDocs;
import kr.co.mapspring.user.oauth.dto.OauthLoginDto;
import kr.co.mapspring.user.oauth.service.OauthLoginCodeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/oauth")
@RequiredArgsConstructor
public class OauthController implements OauthControllerDocs {

    private final OauthLoginCodeService oauthLoginCodeService;

    private final AuthCookieService authCookieService;

    @Override
    @PostMapping("/tokens")
    public ApiResponseDTO<OauthLoginDto.ResponseToken> exchangeToken(
            @Valid @RequestBody OauthLoginDto.RequestToken request,
            HttpServletResponse httpServletResponse
    ) {
        OauthLoginDto.TokenResult tokenResult =
                oauthLoginCodeService.consumeToken(request.getCode());

        // ✅ 로그 추가 - 여기서 null이면 쿠키 내용이 비어서 세팅 안 됨
        System.out.println("=== OAuth Cookie Debug ===");
        System.out.println("refreshToken = " + tokenResult.getRefreshToken());

        String cookieValue = authCookieService
                .createRefreshTokenCookie(tokenResult.getRefreshToken())
                .toString();

        System.out.println("Set-Cookie value = " + cookieValue);

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookieValue);

        OauthLoginDto.ResponseToken response =
                OauthLoginDto.ResponseToken.from(tokenResult);

        return ApiResponseDTO.success("OAuth 토큰 교환 성공", response);
    }
}