package kr.co.mapspring.user.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.user.dto.AdminUserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserDto.ResponseList> getUserList(String keyword) {
        List<User> users;
        if (keyword != null && !keyword.isBlank()) {
            users = userRepository.findByNameContainingOrEmailContaining(keyword, keyword);
        } else {
            users = userRepository.findAll();
        }
        return users.stream()
                .map(AdminUserDto.ResponseList::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserDto.ResponseDetail getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return AdminUserDto.ResponseDetail.from(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, AdminUserDto.RequestUpdateStatus request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateStatus(request.getStatus());
    }
}