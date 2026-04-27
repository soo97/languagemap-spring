package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.global.jwt.RefreshTokenService;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.LoginServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private LoginServiceImpl loginService;
    
    @Test
    @DisplayName("존재하는 이메일과 올바른 비밀번호면 로그인에 성공한다")
    void loginSuccess() {
        // given
        // 로그인 요청 생성
    	LoginDto.RequestLogin request = new LoginDto.RequestLogin("test@naver.com", "1234");
    	
        // 테스트용 사용자 생성
        User user = createUser("encodedPassword");

        // 이메일 조회 시 해당 사용자 반환
        given(userRepository.findByEmail("test@naver.com"))
                .willReturn(Optional.of(user));

        // 비밀번호 일치하도록 mock 설정
        given(passwordEncoder.matches("1234", "encodedPassword"))
                .willReturn(true);
        
    	// JWT Access Token 생성 mock 설정
        given(jwtTokenProvider.createAccessToken(user))
                .willReturn("mock-access-token");
        
        given(jwtTokenProvider.createRefreshToken(user))
        .willReturn("mock-refresh-token");

        // when
        LoginDto.ResponseLogin response = loginService.login(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
        assertThat(response.getAccessToken()).isEqualTo("mock-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("mock-refresh-token");
        verify(refreshTokenService).saveRefreshToken(1L, "mock-refresh-token");
    }

    private User createUser(String passwordHash) {
        User user = User.create(
                "test@naver.com",
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                passwordHash
        );

        // 테스트에서는 DB 저장이 없으므로 userId가 자동 생성되지 않는다.
        // Refresh Token 저장 검증을 위해 테스트용 userId를 넣어준다.
        ReflectionTestUtils.setField(user, "userId", 1L);

        return user;
    }
    
    
    @Test
    @DisplayName("존재하지 않는 이메일이면 예외가 발생한다")
    void loginFailWhenUserNotFound() {
        // given
        // 존재하지 않는 이메일로 로그인 요청을 만든다.
    	LoginDto.RequestLogin request = new LoginDto.RequestLogin("notfound@naver.com", "1234");

        // 해당 이메일로 조회하면 사용자가 없다고 가정한다.
        given(userRepository.findByEmail("notfound@naver.com"))
                .willReturn(Optional.empty());

        // when & then
        // 로그인 시 UserNotFoundException이 발생하는지 확인한다.
        assertThatThrownBy(() -> loginService.login(request))
        .isInstanceOf(CustomException.class)
        .hasMessage("존재하지 않는 이메일입니다.");
    }
    
    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void loginFailWhenPasswordInvalid() {
        // given
        // 로그인 요청을 만든다.
    	LoginDto.RequestLogin request = new LoginDto.RequestLogin("test@naver.com", "wrongPassword");

        // 조회는 성공하는 사용자 객체를 만든다.
        User user = createUser("encodedPassword");

        // 이메일로 사용자 조회 시 해당 사용자를 반환한다.
        given(userRepository.findByEmail("test@naver.com"))
                .willReturn(Optional.of(user));

        // 입력한 비밀번호와 저장된 비밀번호가 일치하지 않는다고 가정한다.
        given(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .willReturn(false);

        // when & then
        // 로그인 시 InvalidPasswordException이 발생하는지 확인한다.
        assertThatThrownBy(() -> loginService.login(request))
        .isInstanceOf(CustomException.class)
        .hasMessage("비밀번호가 일치하지 않습니다.");
    }
    
    @Test
    @DisplayName("사용자 상태가 ACTIVE가 아니면 예외가 발생한다")
    void loginFailWhenUserInactive() {
        // given
        // 정상 이메일과 비밀번호 요청을 만든다.
    	 LoginDto.RequestLogin request = new LoginDto.RequestLogin("test@naver.com", "1234");
        // ACTIVE가 아닌 사용자 객체를 만든다.
        User user = createInactiveUser("encodedPassword");

        // 이메일 조회는 성공한다고 가정한다.
        given(userRepository.findByEmail("test@naver.com"))
                .willReturn(Optional.of(user));

        // 비밀번호도 일치한다고 가정한다.
        given(passwordEncoder.matches("1234", "encodedPassword"))
                .willReturn(true);

        // when & then
        // 상태가 ACTIVE가 아니므로 InactiveUserException이 발생해야 한다.
        assertThatThrownBy(() -> loginService.login(request))
        .isInstanceOf(CustomException.class)
        .hasMessage("비활성 사용자입니다.");
    }
    
    private User createInactiveUser(String passwordHash) {
        User user = User.create(
                "test@naver.com",
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                passwordHash
        );

        // ACTIVE -> INACTIVE 상태로 바꾼다.
        user.deactivate();

        return user;
    }
    
    
}
