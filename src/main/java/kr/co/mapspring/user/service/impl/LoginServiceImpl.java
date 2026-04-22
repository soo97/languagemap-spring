package kr.co.mapspring.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.mapspring.global.exception.user.InactiveUserException;
import kr.co.mapspring.global.exception.user.InvalidPasswordException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.user.dto.LoginRequest;
import kr.co.mapspring.user.dto.LoginResponse;
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
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
        		.orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
        	throw new InvalidPasswordException();
        }

        if (!user.isActive()) {
        	throw new InactiveUserException();
        }

        return LoginResponse.from(user);
    }
}