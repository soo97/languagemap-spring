package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.LoginDto;

public interface LoginService {

    LoginDto.Response login(LoginDto.Request request);

}