package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.SignUpDto;

public interface SignUpService {
	
    SignUpDto.ResponseSignUp signUp(SignUpDto.RequestSignUp request);

}
