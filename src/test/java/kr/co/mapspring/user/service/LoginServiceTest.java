package kr.co.mapspring.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.serivce.LoginService;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;
    
    @Test
    @DisplayName("존재하는 이메일과 올바른 비밀번호면 로그인에 성공한다")
    void loginSuccess() {
        // given

        // when

        // then
    	
    	
    	
    }

}
