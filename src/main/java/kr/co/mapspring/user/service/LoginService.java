package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.LoginRequest;
import kr.co.mapspring.user.dto.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest request);
}