package kr.co.mapspring.user.service.impl;

import org.springframework.stereotype.Service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.global.jwt.RefreshTokenService;
import kr.co.mapspring.user.dto.TokenDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    public TokenServiceImpl(
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenService refreshTokenService,
            UserRepository userRepository
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @Override
    public TokenDto.ResponseReissue reissue(TokenDto.RequestReissue request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);

        if (!refreshTokenService.isRefreshTokenMatched(userId, refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.isActive()) {
            throw new CustomException(ErrorCode.INACTIVE_USER);
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user);

        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

        refreshTokenService.saveRefreshToken(userId, newRefreshToken);

        return TokenDto.ResponseReissue.of(newAccessToken, newRefreshToken);
    }
    
    
    @Override
    public void logout(TokenDto.RequestLogout request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);

        if (!refreshTokenService.isRefreshTokenMatched(userId, refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        refreshTokenService.deleteRefreshToken(userId);
    }
    
}