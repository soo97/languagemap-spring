package kr.co.mapspring.user.service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

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
    
    
}