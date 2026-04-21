package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.Impl.LoginServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImpl  loginService;
    
    @Test
    @DisplayName("존재하는 이메일과 올바른 비밀번호면 로그인에 성공한다")
    void loginSuccess() {
        // given
    	LoginRequest request = new LoginRequest("test@naver.com", "1234");
    	User user = creatUser("encodedPassword", UserStatus.ACTIVE);
    	
    	given(userRepository.findByEmail("test@naver.com"))
    			.willReturn(Optional.of(user));
    	given(passwordEncoder.matches("1234","encodedPassword"))
    			.willReturn(true);
        // when
    	LoginResponse response = loginService.login(request);

        // then
    	assertThat(response).isNotNull();
    	assertThat(response.getEmail()).isEqualTo("test@naver.com");
    	assertThat(response.getName()).isEqualTo("홍길동");
    }
    
    private User creatUser(String passwordHash, UserStatus status) {
    	return new User(
    			"test@naver.com",
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                passwordHash,
                status,
                UserRole.USER
    	);
    }
}
