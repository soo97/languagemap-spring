package kr.co.mapspring.user.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.controller.docs.AuthControllerDocs;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.service.LoginService;
import kr.co.mapspring.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    // 로그인 서비스
    private final LoginService loginService;

    // 회원가입 서비스
    private final SignUpService signUpService;

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
}