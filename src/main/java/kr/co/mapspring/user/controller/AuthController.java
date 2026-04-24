package kr.co.mapspring.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.service.LoginService;
import kr.co.mapspring.user.service.SignUpService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final SignUpService signUpService;

    @PostMapping("/login")
    public ApiResponseDTO<LoginDto.ResponseLogin> login(
            @RequestBody LoginDto.RequestLogin request
    ) {
        LoginDto.ResponseLogin response = loginService.login(request);
        return ApiResponseDTO.success("로그인 성공", response);
    }

    @PostMapping("/signup")
    public ApiResponseDTO<SignUpDto.ResponseSignUp> signUp(
            @Valid @RequestBody SignUpDto.RequestSignUp request
    ) {
        SignUpDto.ResponseSignUp response = signUpService.signUp(request);
        return ApiResponseDTO.success("회원가입 성공", response);
    }
}