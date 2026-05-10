package kr.co.mapspring.user.service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.user.dto.AdminUserDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import kr.co.mapspring.user.service.impl.AdminUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    @Test
    @DisplayName("키워드 없이 전체 회원 목록 조회에 성공한다")
    void getUserListSuccessWithoutKeyword() {
        // given
        User user = createUser();
        given(userRepository.findAll())
                .willReturn(List.of(user));

        // when
        List<AdminUserDto.ResponseList> result = adminUserService.getUserList(null);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("test@naver.com");
        assertThat(result.get(0).getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("키워드로 회원 목록 검색에 성공한다")
    void getUserListSuccessWithKeyword() {
        // given
        User user = createUser();
        given(userRepository.findByNameContainingOrEmailContaining("홍길동", "홍길동"))
                .willReturn(List.of(user));

        // when
        List<AdminUserDto.ResponseList> result = adminUserService.getUserList("홍길동");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("키워드 검색 결과가 없으면 빈 리스트를 반환한다")
    void getUserListEmptyWithKeyword() {
        // given
        given(userRepository.findByNameContainingOrEmailContaining("없는사람", "없는사람"))
                .willReturn(List.of());

        // when
        List<AdminUserDto.ResponseList> result = adminUserService.getUserList("없는사람");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유효한 userId면 회원 상세 조회에 성공한다")
    void getUserDetailSuccess() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        // when
        AdminUserDto.ResponseDetail result = adminUserService.getUserDetail(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("존재하지 않는 userId면 회원 상세 조회 시 예외가 발생한다")
    void getUserDetailFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> adminUserService.getUserDetail(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }

    @Test
    @DisplayName("유효한 userId면 회원 상태 변경에 성공한다")
    void updateUserStatusSuccess() {
        // given
        User user = createUser();
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        AdminUserDto.RequestUpdateStatus request =
                new AdminUserDto.RequestUpdateStatus(UserStatus.SUSPENDED);

        // when & then
        assertThatCode(() -> adminUserService.updateUserStatus(1L, request))
                .doesNotThrowAnyException();

        assertThat(user.getStatus()).isEqualTo(UserStatus.SUSPENDED);
    }

    @Test
    @DisplayName("존재하지 않는 userId면 상태 변경 시 예외가 발생한다")
    void updateUserStatusFailWhenUserNotFound() {
        // given
        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        AdminUserDto.RequestUpdateStatus request =
                new AdminUserDto.RequestUpdateStatus(UserStatus.SUSPENDED);

        // when & then
        assertThatThrownBy(() -> adminUserService.updateUserStatus(999L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }

    private User createUser() {
        User user = User.create(
                "test@naver.com",
                "홍길동",
                LocalDate.of(2000, 1, 1),
                "서울시 강남구",
                "01012345678",
                "encodedPassword"
        );
        ReflectionTestUtils.setField(user, "userId", 1L);
        return user;
    }
}