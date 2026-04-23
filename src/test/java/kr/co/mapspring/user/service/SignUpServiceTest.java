package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;

import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.SignUpServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignUpServiceImpl signUpService;

    @Test
    @DisplayName("중복되지 않은 이메일이면 회원가입에 성공한다")
    void signUpSuccess() {
        // given
        // 회원가입 요청 객체를 만든다.
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                "2000-01-01",
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234"
        );

        // 해당 이메일이 아직 존재하지 않는다고 가정한다.
        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        // 비밀번호 암호화 결과를 가정한다.
        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        // 저장 후 반환될 User 객체를 만든다.
        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        // save 호출 시 위 객체를 반환한다고 가정한다.
        given(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .willReturn(savedUser);

        // when
        SignUpDto.ResponseSignUp response = signUpService.signUp(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
    }
}