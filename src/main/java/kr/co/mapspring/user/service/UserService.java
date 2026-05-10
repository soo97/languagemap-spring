package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.UserDto;

public interface UserService {

    /*
     * 현재 로그인한 유저의 정보를 조회합니다.
     */
    UserDto.ResponseMe getMe(Long userId);
    
    UserDto.ResponseProfileSetup setupProfile(Long userId, UserDto.RequestProfileSetup request);
    
    UserDto.ResponseUpdateMe updateMe(Long userId, UserDto.RequestUpdateMe request);
    
    void changePassword(Long userId, UserDto.RequestChangePassword request);
    
    void deleteMe(Long userId);
}