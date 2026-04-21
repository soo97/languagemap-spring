package kr.co.mapspring.user.service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.mapspring.user.dto.LoginRequest;
import kr.co.mapspring.user.dto.LoginResponse;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }
}