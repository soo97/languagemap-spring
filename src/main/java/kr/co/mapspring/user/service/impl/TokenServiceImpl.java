package kr.co.mapspring.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.user.InactiveUserException;
import kr.co.mapspring.global.exception.user.InvalidRefreshTokenException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.global.jwt.RefreshTokenService;
import kr.co.mapspring.user.dto.TokenDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.TokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public TokenDto.ResponseReissue reissue(TokenDto.RequestReissue request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);

        if (!refreshTokenService.isRefreshTokenMatched(userId, refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActive()) {
            throw new InactiveUserException();
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