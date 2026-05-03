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
        /*
         * 프론트가 전달한 1회용 code로 Redis에 임시 저장된 토큰 정보를 조회합니다.
         * code는 조회 후 즉시 삭제됩니다.
         */
        OauthLoginDto.TokenResult tokenResult =
                oauthLoginCodeService.consumeToken(request.getCode());

        /*
         * refreshToken은 JSON body에 노출하지 않고 HttpOnly Cookie로 내려보냅니다.
         */
        httpServletResponse.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService.createRefreshTokenCookie(tokenResult.getRefreshToken()).toString()
        );

        /*
         * 응답 body에는 accessToken과 profileRequired만 포함됩니다.
         */
        OauthLoginDto.ResponseToken response =
                OauthLoginDto.ResponseToken.from(tokenResult);

        return ApiResponseDTO.success("OAuth 토큰 교환 성공", response);
    }
}