package kr.co.mapspring.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.service.LoginService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // 로그인 비즈니스 로직은 서비스가 담당한다.
    private final LoginService loginService;

    @PostMapping("/login")
    public ApiResponseDTO<LoginDto.ResponseLogin> login(
            @RequestBody LoginDto.RequestLogin request
    ) {
        LoginDto.ResponseLogin response = loginService.login(request);
        return ApiResponseDTO.success("로그인 성공", response);
    }
}