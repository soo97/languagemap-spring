package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.UserDto;

public interface UserService {

    /*
     * 현재 로그인한 유저의 정보를 조회합니다.
     */
    UserDto.ResponseMe getMe(Long userId);
}