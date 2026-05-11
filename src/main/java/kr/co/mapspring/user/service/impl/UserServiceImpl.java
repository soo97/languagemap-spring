package kr.co.mapspring.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDto.ResponseMe getMe(Long userId) {
        /*
         * userId로 유저를 조회합니다..
         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserDto.ResponseMe.from(user);
    }
    
    
    @Override
    @Transactional
    public UserDto.ResponseProfileSetup setupProfile(Long userId, UserDto.RequestProfileSetup request) {
        /*
         * userId로 유저를 조회합니다.
         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        /*
         * 전화번호 중복 체크
         */
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new CustomException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
        

        /*
         * 소셜 유저 프로필을 저장합니다.
         */
        user.setupProfile(
                request.getBirthDate(),
                request.getAddress(),
                request.getPhoneNumber()
        );

        return UserDto.ResponseProfileSetup.from(user);
    }    
    
    @Override
    @Transactional
    public UserDto.ResponseUpdateMe updateMe(Long userId, UserDto.RequestUpdateMe request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 전화번호 중복 체크 (변경하는 경우에만)
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            if (userRepository.existsByPhoneNumberAndUserIdNot(request.getPhoneNumber(), userId)) {
                throw new CustomException(ErrorCode.DUPLICATE_PHONE_NUMBER);
            }
        }

        user.updateProfile(
                request.getName(),
                request.getBirthDate(),
                request.getAddress(),
                request.getPhoneNumber()
        );

        return UserDto.ResponseUpdateMe.from(user);
    }
    
    @Override
    @Transactional
    public void changePassword(Long userId, UserDto.RequestChangePassword request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 소셜 유저 체크 (passwordHash가 null이면 소셜 유저)
        if (user.getPasswordHash() == null) {
            throw new CustomException(ErrorCode.SOCIAL_USER_CANNOT_CHANGE_PASSWORD);
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new CustomException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        // 새 비밀번호 확인
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }
	    @Override
	    @Transactional
	    public void deleteMe(Long userId) {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	
	        if (user.getStatus() == UserStatus.DELETED) {
	            throw new CustomException(ErrorCode.ALREADY_DELETED_USER);
	        }
	
	        user.delete();
    }
    
    
}