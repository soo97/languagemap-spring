package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.dto.LoginDto.RequestLogin;
import kr.co.mapspring.user.dto.LoginDto.ResponseLogin;

public interface LoginService {

    ResponseLogin login(RequestLogin request);

}