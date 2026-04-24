package kr.co.mapspring.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.user.EmailAlreadyExistsException;
import kr.co.mapspring.global.exception.user.PasswordConfirmMismatchException;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.SignUpService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignUpDto.ResponseSignUp signUp(SignUpDto.RequestSignUp request) {
        if (userRepository.existsByEmail(request.getEmail())) {
        	throw new EmailAlreadyExistsException();
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
        	throw new PasswordConfirmMismatchException();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());


        User user = User.create(
                request.getEmail(),
                request.getName(), 	
                request.getBirthDate(),
                request.getAddress(),
                request.getPhoneNumber(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        return SignUpDto.ResponseSignUp.from(savedUser);
    }
}