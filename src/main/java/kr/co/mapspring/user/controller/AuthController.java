package kr.co.mapspring.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.global.jwt.AuthCookieService;
import kr.co.mapspring.user.controller.docs.AuthControllerDocs;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.dto.TokenDto;
import kr.co.mapspring.user.service.LoginService;
import kr.co.mapspring.user.service.SignUpService;
import kr.co.mapspring.user.service.TokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    // 로그인 서비스
    private final LoginService loginService;

    // 회원가입 서비스
    private final SignUpService signUpService;

    // 토큰 재발급 / 로그아웃 서비스
    private final TokenService tokenService;

    // Refresh Token Cookie 생성 / 추출 / 삭제 담당
    private final AuthCookieService authCookieService;

    @Override
    @PostMapping("/login")
    public ApiResponseDTO<LoginDto.ResponseLogin> login(
            @Valid @RequestBody LoginDto.RequestLogin request,
            HttpServletResponse httpServletResponse
    ) {
        /*
         * 로그인 성공 시 accessToken + refreshToken이 생성됩니다.
         * 단, refreshToken은 JSON 응답에 노출하지 않고 HttpOnly Cookie로 내려보냅니다.
         */
        LoginDto.ResponseLogin response = loginService.login(request);

        /*
         * refreshToken을 HttpOnly Cookie로 설정합니다.
         * LoginDto.ResponseLogin의 refreshToken 필드는 @JsonIgnore 처리되어
         * 응답 body에는 포함되지 않습니다.
         */
        httpServletResponse.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService.createRefreshTokenCookie(response.getRefreshToken()).toString()
        );

        /*
         * 응답 body에는 accessToken만 포함됩니다.
         */
        return ApiResponseDTO.success("로그인 성공", response);
    }

    @Override
    @PostMapping("/signup")
    public ApiResponseDTO<SignUpDto.ResponseSignUp> signUp(
            @Valid @RequestBody SignUpDto.RequestSignUp request
    ) {
        /*
         * 회원가입 요청값 검증은 DTO의 Bean Validation과
         * SignUpService의 비즈니스 검증에서 처리합니다.
         */
        SignUpDto.ResponseSignUp response = signUpService.signUp(request);

        return ApiResponseDTO.success("회원가입 성공", response);
    }

    @Override
    @PostMapping("/tokens")
    public ApiResponseDTO<TokenDto.ResponseReissue> reissueToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        /*
         * 기존 방식:
         * - request body에서 refreshToken을 받음
         *
         * 변경 방식:
         * - HttpOnly Cookie에서 refreshToken을 추출함
         */
        String refreshToken = authCookieService.extractRefreshToken(httpServletRequest);

        /*
         * 기존 TokenService 시그니처를 유지하기 위해
         * Cookie에서 꺼낸 refreshToken으로 RequestReissue DTO를 생성합니다.
         */
        TokenDto.RequestReissue request = new TokenDto.RequestReissue(refreshToken);

        TokenDto.ResponseReissue response = tokenService.reissue(request);

        /*
         * Refresh Token Rotation 후 새 refreshToken을 다시 HttpOnly Cookie로 내려보냅니다.
         * ResponseReissue의 refreshToken 필드는 @JsonIgnore 처리되어 body에는 노출되지 않습니다.
         */
        httpServletResponse.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService.createRefreshTokenCookie(response.getRefreshToken()).toString()
        );

        return ApiResponseDTO.success("토큰 재발급 성공", response);
    }

    @Override
    @PostMapping("/logout")
    public ApiResponseDTO<Void> logout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        /*
         * 로그아웃도 request body가 아니라 Cookie에서 refreshToken을 추출합니다.
         */
        String refreshToken = authCookieService.extractRefreshToken(httpServletRequest);

        TokenDto.RequestLogout request = new TokenDto.RequestLogout(refreshToken);

        /*
         * Redis에 저장된 refresh:{userId} 값을 삭제합니다.
         */
        tokenService.logout(request);

        /*
         * 브라우저의 refreshToken Cookie도 만료시킵니다.
         */
        httpServletResponse.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService.deleteRefreshTokenCookie().toString()
        );

        return ApiResponseDTO.success("로그아웃 성공");
    }
}