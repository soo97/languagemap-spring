package kr.co.mapspring.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.dto.LoginDto.RequestLogin;
import kr.co.mapspring.user.dto.LoginDto.ResponseLogin;
import kr.co.mapspring.user.entity.User;
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
    public ResponseLogin login(RequestLogin request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        if (!user.isActive()) {
            throw new CustomException(ErrorCode.INACTIVE_USER);
        }

        return LoginDto.ResponseLogin.from(user);
    }
}