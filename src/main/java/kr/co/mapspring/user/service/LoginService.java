package kr.co.mapspring.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.mapspring.user.repository.UserRepository;

public class LoginService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
}
