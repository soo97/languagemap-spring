package kr.co.mapspring.user.service;

import java.util.List;
import kr.co.mapspring.user.dto.AdminUserDto;

public interface AdminUserService {
    List<AdminUserDto.ResponseList> getUserList(String keyword);
    AdminUserDto.ResponseDetail getUserDetail(Long userId);
    void updateUserStatus(Long userId, AdminUserDto.RequestUpdateStatus request);
}