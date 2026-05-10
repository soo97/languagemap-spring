package kr.co.mapspring.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유효한 userId면 내 정보 조회에 성공한다")
    void getMeSuccess() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // when
        UserDto.ResponseMe response = userService.getMe(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
        assertThat(response.getRole()).isEqualTo("USER");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("존재하지 않는 userId면 예외가 발생한다")
    void getMeFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getMe(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
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
        ReflectionTestUtils.setField(user, "userId", 1L);
        return user;
    }
    
    @Test
    @DisplayName("유효한 userId면 프로필 입력에 성공한다")
    void setupProfileSuccess() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        UserDto.RequestProfileSetup request = new UserDto.RequestProfileSetup(
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-9999-8888"
        );

        // when
        UserDto.ResponseProfileSetup response = userService.setupProfile(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@naver.com");
        assertThat(response.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("존재하지 않는 userId면 프로필 입력 시 예외가 발생한다")
    void setupProfileFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        UserDto.RequestProfileSetup request = new UserDto.RequestProfileSetup(
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "010-9999-8888"
        );

        // when & then
        assertThatThrownBy(() -> userService.setupProfile(999L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }
    
    @Test
    @DisplayName("유효한 userId면 내 정보 수정에 성공한다")
    void updateMeSuccess() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        given(userRepository.existsByPhoneNumberAndUserIdNot("01099998888", 1L))
                .willReturn(false);

        UserDto.RequestUpdateMe request = new UserDto.RequestUpdateMe(
                "김철수",
                LocalDate.of(1995, 5, 5),
                "서울시 서초구",
                "01099998888"
        );

        // when
        UserDto.ResponseUpdateMe response = userService.updateMe(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("김철수");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 5));
        assertThat(response.getAddress()).isEqualTo("서울시 서초구");
        assertThat(response.getPhoneNumber()).isEqualTo("01099998888");
    }

    @Test
    @DisplayName("존재하지 않는 userId면 내 정보 수정 시 예외가 발생한다")
    void updateMeFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        UserDto.RequestUpdateMe request = new UserDto.RequestUpdateMe(
                "김철수",
                LocalDate.of(1995, 5, 5),
                "서울시 서초구",
                "01099998888"
        );

        // when & then
        assertThatThrownBy(() -> userService.updateMe(999L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }

    @Test
    @DisplayName("이미 사용 중인 전화번호로 수정 시 예외가 발생한다")
    void updateMeFailWhenDuplicatePhoneNumber() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        given(userRepository.existsByPhoneNumberAndUserIdNot("01099998888", 1L))
                .willReturn(true);

        UserDto.RequestUpdateMe request = new UserDto.RequestUpdateMe(
                "김철수",
                LocalDate.of(1995, 5, 5),
                "서울시 서초구",
                "01099998888"
        );

        // when & then
        assertThatThrownBy(() -> userService.updateMe(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 사용 중인 전화번호입니다.");
    }

    @Test
    @DisplayName("null 필드는 수정하지 않는다")
    void updateMeWithNullFields() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        UserDto.RequestUpdateMe request = new UserDto.RequestUpdateMe(
                null, null, null, null
        );

        // when
        UserDto.ResponseUpdateMe response = userService.updateMe(1L, request);

        // then
        assertThat(response.getName()).isEqualTo("홍길동");
        assertThat(response.getAddress()).isEqualTo("서울시 강남구");
    }
    
    
    @Test
    @DisplayName("유효한 userId면 비밀번호 변경에 성공한다")
    void changePasswordSuccess() {
        // given
        User user = createUser();
        ReflectionTestUtils.setField(user, "passwordHash", "encodedPassword");
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches("currentPassword1!", "encodedPassword"))
                .willReturn(true);
        given(passwordEncoder.encode("newPassword1!"))
                .willReturn("newEncodedPassword");

        UserDto.RequestChangePassword request = new UserDto.RequestChangePassword(
                "currentPassword1!",
                "newPassword1!",
                "newPassword1!"
        );

        // when & then (void 메서드라 예외 없으면 성공)
        assertThatCode(() -> userService.changePassword(1L, request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("소셜 유저는 비밀번호 변경 시 예외가 발생한다")
    void changePasswordFailWhenSocialUser() {
        // given
        User user = User.createOauthUser("test@gmail.com", "홍길동");
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        UserDto.RequestChangePassword request = new UserDto.RequestChangePassword(
                "currentPassword1!",
                "newPassword1!",
                "newPassword1!"
        );

        // when & then
        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("소셜 로그인 유저는 비밀번호를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("현재 비밀번호가 틀리면 예외가 발생한다")
    void changePasswordFailWhenWrongCurrentPassword() {
        // given
        User user = createUser();
        ReflectionTestUtils.setField(user, "passwordHash", "encodedPassword");
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongPassword!", "encodedPassword"))
                .willReturn(false);

        UserDto.RequestChangePassword request = new UserDto.RequestChangePassword(
                "wrongPassword!",
                "newPassword1!",
                "newPassword1!"
        );

        // when & then
        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("현재 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("새 비밀번호와 확인이 다르면 예외가 발생한다")
    void changePasswordFailWhenPasswordConfirmMismatch() {
        // given
        User user = createUser();
        ReflectionTestUtils.setField(user, "passwordHash", "encodedPassword");
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches("currentPassword1!", "encodedPassword"))
                .willReturn(true);

        UserDto.RequestChangePassword request = new UserDto.RequestChangePassword(
                "currentPassword1!",
                "newPassword1!",
                "differentPassword1!"
        );

        // when & then
        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 userId면 비밀번호 변경 시 예외가 발생한다")
    void changePasswordFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        UserDto.RequestChangePassword request = new UserDto.RequestChangePassword(
                "currentPassword1!",
                "newPassword1!",
                "newPassword1!"
        );

        // when & then
        assertThatThrownBy(() -> userService.changePassword(999L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }
    @Test
    @DisplayName("유효한 userId면 회원 탈퇴에 성공한다")
    void deleteMeSuccess() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // when & then
        assertThatCode(() -> userService.deleteMe(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 탈퇴한 유저면 예외가 발생한다")
    void deleteMeFailWhenAlreadyDeleted() {
        // given
        User user = createUser();
        user.delete();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> userService.deleteMe(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 탈퇴한 회원입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 userId면 회원 탈퇴 시 예외가 발생한다")
    void deleteMeFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.deleteMe(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }
    
    
}