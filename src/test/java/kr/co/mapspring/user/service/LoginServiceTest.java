package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.mapspring.user.dto.LoginRequest;
import kr.co.mapspring.user.dto.LoginResponse;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.LoginServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImpl loginService;
    
    @Test
    @DisplayName("존재하는 이메일과 올바른 비밀번호면 로그인에 성공한다")
    void loginSuccess() {
        // given
        // 로그인 요청 생성
        LoginRequest request = new LoginRequest("test@naver.com", "1234");

        // 테스트용 사용자 생성
        User user = createUser("encodedPassword");

        // 이메일 조회 시 해당 사용자 반환
        given(userRepository.findByEmail("test@naver.com"))
                .willReturn(Optional.of(user));

        // 비밀번호 일치하도록 mock 설정
        given(passwordEncoder.matches("1234", "encodedPassword"))
                .willReturn(true);

        // when
        LoginResponse response = loginService.login(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
    }

    private User createUser(String passwordHash) {
        // 엔티티 내부 생성 규칙을 사용해서 테스트 사용자 생성
        return User.create(
                "test@naver.com",
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                passwordHash
        );
    }
    
    
    @Test
    @DisplayName("존재하지 않는 이메일이면 예외가 발생한다")
    void loginFailWhenUserNotFound() {
        // given
        // 존재하지 않는 이메일로 로그인 요청을 만든다.
        LoginRequest request = new LoginRequest("notfound@naver.com", "1234");

        // 해당 이메일로 조회하면 사용자가 없다고 가정한다.
        given(userRepository.findByEmail("notfound@naver.com"))
                .willReturn(Optional.empty());

        // when & then
        // 로그인 시 UserNotFoundException이 발생하는지 확인한다.
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(UserNotFoundException.class);
    }
    
    
    
    
    
    
    
}
