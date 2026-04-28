package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.global.jwt.RefreshTokenService;
import kr.co.mapspring.user.dto.TokenDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.TokenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    @DisplayName("유효한 Refresh Token이면 새로운 Access Token과 Refresh Token을 발급한다")
    void reissueSuccess() {
        // given
        String oldRefreshToken = "old-refresh-token";
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";

        TokenDto.RequestReissue request = new TokenDto.RequestReissue(oldRefreshToken);

        User user = createUser();

        // 기존 Refresh Token 자체가 유효하다고 가정한다.
        given(jwtTokenProvider.validateRefreshToken(oldRefreshToken))
                .willReturn(true);

        // Refresh Token에서 userId를 꺼낸다고 가정한다.
        given(jwtTokenProvider.getUserId(oldRefreshToken))
                .willReturn(1L);

        // Redis에 저장된 Refresh Token과 요청 Refresh Token이 같다고 가정한다.
        given(refreshTokenService.isRefreshTokenMatched(1L, oldRefreshToken))
                .willReturn(true);

        // userId로 사용자 조회가 성공한다고 가정한다.
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // 새 Access Token이 발급된다고 가정한다.
        given(jwtTokenProvider.createAccessToken(user))
                .willReturn(newAccessToken);

        // 새 Refresh Token이 발급된다고 가정한다.
        given(jwtTokenProvider.createRefreshToken(user))
                .willReturn(newRefreshToken);

        // when
        TokenDto.ResponseReissue response = tokenService.reissue(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(response.getRefreshToken()).isEqualTo(newRefreshToken);

        // 새 Refresh Token이 Redis 저장 서비스로 전달됐는지 확인한다.
        verify(refreshTokenService).saveRefreshToken(1L, newRefreshToken);
    }

    @Test
    @DisplayName("Refresh Token 자체가 유효하지 않으면 예외가 발생한다")
    void reissueFailWhenRefreshTokenInvalid() {
        // given
        String refreshToken = "invalid-refresh-token";
        TokenDto.RequestReissue request = new TokenDto.RequestReissue(refreshToken);

        given(jwtTokenProvider.validateRefreshToken(refreshToken))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    @DisplayName("Redis에 저장된 Refresh Token과 일치하지 않으면 예외가 발생한다")
    void reissueFailWhenRefreshTokenDoesNotMatchRedisValue() {
        // given
        String refreshToken = "valid-but-not-matched-refresh-token";
        TokenDto.RequestReissue request = new TokenDto.RequestReissue(refreshToken);

        given(jwtTokenProvider.validateRefreshToken(refreshToken))
                .willReturn(true);

        given(jwtTokenProvider.getUserId(refreshToken))
                .willReturn(1L);

        given(refreshTokenService.isRefreshTokenMatched(1L, refreshToken))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 예외가 발생한다")
    void reissueFailWhenUserNotFound() {
        // given
        String refreshToken = "valid-refresh-token";
        TokenDto.RequestReissue request = new TokenDto.RequestReissue(refreshToken);

        given(jwtTokenProvider.validateRefreshToken(refreshToken))
                .willReturn(true);

        given(jwtTokenProvider.getUserId(refreshToken))
                .willReturn(1L);

        given(refreshTokenService.isRefreshTokenMatched(1L, refreshToken))
                .willReturn(true);

        given(userRepository.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("사용자 상태가 ACTIVE가 아니면 토큰 재발급에 실패한다")
    void reissueFailWhenUserInactive() {
        // given
        String refreshToken = "valid-refresh-token";
        TokenDto.RequestReissue request = new TokenDto.RequestReissue(refreshToken);

        User inactiveUser = createInactiveUser();

        given(jwtTokenProvider.validateRefreshToken(refreshToken))
                .willReturn(true);

        given(jwtTokenProvider.getUserId(refreshToken))
                .willReturn(1L);

        given(refreshTokenService.isRefreshTokenMatched(1L, refreshToken))
                .willReturn(true);

        given(userRepository.findById(1L))
                .willReturn(Optional.of(inactiveUser));

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INACTIVE_USER.getMessage());
    }

    private User createUser() {
        User user = User.create(
                "test@naver.com",
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "encodedPassword"
        );

        // 테스트에서는 DB 저장이 없으므로 userId가 자동 생성되지 않는다.
        ReflectionTestUtils.setField(user, "userId", 1L);

        return user;
    }

    private User createInactiveUser() {
        User user = createUser();

        // ACTIVE -> INACTIVE 상태로 변경
        user.deactivate();

        return user;
    }
    
    @Test
    @DisplayName("유효한 Refresh Token이면 로그아웃에 성공하고 Redis에서 Refresh Token을 삭제한다")
    void logoutSuccess() {
        // given
        String refreshToken = "valid-refresh-token";
        TokenDto.RequestLogout request = new TokenDto.RequestLogout(refreshToken);

        // Refresh Token 자체가 유효하다고 가정한다.
        given(jwtTokenProvider.validateRefreshToken(refreshToken))
                .willReturn(true);

        // Refresh Token에서 userId를 꺼낸다고 가정한다.
        given(jwtTokenProvider.getUserId(refreshToken))
                .willReturn(1L);

        // Redis에 저장된 Refresh Token과 요청 Refresh Token이 같다고 가정한다.
        given(refreshTokenService.isRefreshTokenMatched(1L, refreshToken))
                .willReturn(true);

        // when
        tokenService.logout(request);

        // then
        // Redis에서 refresh:1이 삭제되는지 확인한다.
        verify(refreshTokenService).deleteRefreshToken(1L);
    }
    
}