package kr.co.mapspring.user.service.impl;

import java.time.LocalDate;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.entity.User;
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
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 존재하는 이메일입니다.");
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        LocalDate birthDate = LocalDate.parse(request.getBirthDate());

        User user = User.create(
                request.getEmail(),
                request.getName(), 	
                birthDate,
                request.getAddress(),
                request.getPhoneNumber(),
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        return SignUpDto.ResponseSignUp.from(savedUser);
    }
}