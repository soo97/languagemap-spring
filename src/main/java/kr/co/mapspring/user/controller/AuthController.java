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

    private final LoginService loginService;

    @PostMapping("/login")
    public ApiResponseDTO<LoginDto.ResponseLogin> login(
            @RequestBody LoginDto.RequestLogin request
    ) {
        LoginDto.ResponseLogin response = loginService.login(request);
        
        return ApiResponseDTO.success("로그인 성공", response);
    }
}