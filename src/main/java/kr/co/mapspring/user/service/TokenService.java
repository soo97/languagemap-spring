package kr.co.mapspring.user.service;

import kr.co.mapspring.user.dto.TokenDto;

public interface TokenService {

    // Refresh Token을 검증한 뒤 Access Token과 Refresh Token을 재발급한다.
    TokenDto.ResponseReissue reissue(TokenDto.RequestReissue request);
}