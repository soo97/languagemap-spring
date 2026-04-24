package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.SignUpServiceImpl;

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
                LocalDate.of(2000, 1, 1),
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
    
    @Test
    @DisplayName("이미 존재하는 이메일이면 예외가 발생한다")
    void signUpFailWhenEmailAlreadyExists() {
        // given
        // 이미 가입된 이메일로 회원가입 요청을 만든다.
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234"
        );

        // 해당 이메일이 이미 존재한다고 가정한다.
        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(true);

        // when & then
        // 회원가입 시 CustomException이 발생하고,
        // 메시지가 "이미 존재하는 이메일입니다."인지 확인한다.
        assertThatThrownBy(() -> signUpService.signUp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }
    
    @Test
    @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않으면 예외가 발생한다")
    void signUpFailWhenPasswordConfirmDoesNotMatch() {
        // given
        // 비밀번호와 비밀번호 확인값이 서로 다른 회원가입 요청을 만든다.
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "4321"
        );

        // 이메일은 중복되지 않는다고 가정한다.
        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        // when & then
        // 회원가입 시 CustomException이 발생하고,
        // 메시지가 비밀번호 불일치 메시지인지 확인한다.
        assertThatThrownBy(() -> signUpService.signUp(request))
                .isInstanceOf(CustomException.class)
                .hasMessage("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
    
    @Test
    @DisplayName("회원가입 시 비밀번호는 암호화되어 저장된다")
    void signUpPasswordShouldBeEncodedBeforeSave() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234"
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(java.time.LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(kr.co.mapspring.user.enums.UserStatus.ACTIVE)
                .role(kr.co.mapspring.user.enums.UserRole.USER)
                .build();

        given(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .willReturn(savedUser);

        // when
        signUpService.signUp(request);

        // then
        // save에 실제로 전달된 User 객체를 캡처한다.
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        // 저장 직전 User의 비밀번호가 평문이 아닌 암호화값인지 확인
        assertThat(capturedUser.getPasswordHash()).isEqualTo("encodedPassword");
    }
    
    @Test
    @DisplayName("회원가입 시 기본 상태는 ACTIVE이고 권한은 USER다")
    void signUpShouldSaveDefaultStatusAndRole() {
        // given
        SignUpDto.RequestSignUp request = new SignUpDto.RequestSignUp(
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "test@naver.com",
                "1234",
                "1234"
        );

        given(userRepository.existsByEmail("test@naver.com"))
                .willReturn(false);

        given(passwordEncoder.encode("1234"))
                .willReturn("encodedPassword");

        User savedUser = User.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .birthDate(java.time.LocalDate.of(2000, 1, 1))
                .address("서울시 강남구")
                .phoneNumber("010-1234-5678")
                .passwordHash("encodedPassword")
                .status(kr.co.mapspring.user.enums.UserStatus.ACTIVE)
                .role(kr.co.mapspring.user.enums.UserRole.USER)
                .build();

        given(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .willReturn(savedUser);

        // when
        signUpService.signUp(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getStatus())
                .isEqualTo(kr.co.mapspring.user.enums.UserStatus.ACTIVE);
        assertThat(capturedUser.getRole())
                .isEqualTo(kr.co.mapspring.user.enums.UserRole.USER);
    }
    
    
}