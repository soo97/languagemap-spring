package kr.co.mapspring.user.service.impl;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
         * 소셜 유저 프로필을 저장합니다.
         */
        user.setupProfile(
                request.getBirthDate(),
                request.getAddress(),
                request.getPhoneNumber()
        );

        return UserDto.ResponseProfileSetup.from(user);
    }    
}