package kr.co.mapspring.user.service.impl;

import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpDto.ResponseSignUp signUp(SignUpDto.RequestSignUp request) {
        return null;
    }
}