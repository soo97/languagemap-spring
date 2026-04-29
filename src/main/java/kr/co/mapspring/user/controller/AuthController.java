package kr.co.mapspring.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
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
    
    // 토큰 서비스
    private final TokenService tokenService;

    @Override
    @PostMapping("/login")
    public ApiResponseDTO<LoginDto.ResponseLogin> login(
            @RequestBody LoginDto.RequestLogin request
    ) {
        // 로그인 서비스 호출
        LoginDto.ResponseLogin response = loginService.login(request);

        // 공통 성공 응답 반환
        return ApiResponseDTO.success("로그인 성공", response);
    }

    @Override
    @PostMapping("/signup")
    public ApiResponseDTO<SignUpDto.ResponseSignUp> signUp(
            @RequestBody SignUpDto.RequestSignUp request
    ) {
        // 회원가입 서비스 호출
        SignUpDto.ResponseSignUp response = signUpService.signUp(request);

        // 공통 성공 응답 반환
        return ApiResponseDTO.success("회원가입 성공", response);
    }
    
    @Override
    @PostMapping("/tokens")
    public ApiResponseDTO<TokenDto.ResponseReissue> reissueToken(
            @Valid @RequestBody TokenDto.RequestReissue request
    ) {
        // Refresh Token 기반 토큰 재발급 서비스 호출
        TokenDto.ResponseReissue response = tokenService.reissue(request);

        // 공통 성공 응답 반환
        return ApiResponseDTO.success("토큰 재발급 성공", response);
    }
    @Override
    @PostMapping("/logout")
    public ApiResponseDTO<Void> logout(
            @Valid @RequestBody TokenDto.RequestLogout request
    ) {
        // Refresh Token 검증 후 Redis에서 삭제
        tokenService.logout(request);

        // 공통 성공 응답 반환
        return ApiResponseDTO.success("로그아웃 성공");
    }
    
}