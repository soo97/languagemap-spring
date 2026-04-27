package kr.co.mapspring.learning.service;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.global.exception.learning.UserGoalNotFoundException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.UserGoalStatus;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.impl.LearningGoalServiceImpl;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LearningGoalServiceTest {

    @Mock
    private GoalMasterRepository goalMasterRepository;

    @Mock
    private UserGoalRepository userGoalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LearningGoalServiceImpl learningGoalService;

    private Long userId;
    private Long goalMasterId;
    private User user;
    private GoalMaster goalMaster;

    @BeforeEach
    void setUp() {
        userId = 1L;
        goalMasterId = 100L;

        user = User.builder()
                .userId(userId)
                .email("test@test.com")
                .name("테스트")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울")
                .phoneNumber("010-1234-5678")
                .passwordHash("1234")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        goalMaster = GoalMaster.of(
                goalMasterId,
                "하루 1회 학습",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                1
        );
    }

    @Test
    @DisplayName("학습 목표를 정상적으로 선택한다")
    void 학습_목표를_정상적으로_선택한다() {
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(false);

        given(userGoalRepository.countByUser_UserId(userId))
                .willReturn(2);

        learningGoalService.selectGoal(userId, goalMasterId);

        verify(userRepository).findById(userId);
        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository).countByUser_UserId(userId);

        ArgumentCaptor<UserGoal> captor = ArgumentCaptor.forClass(UserGoal.class);
        verify(userGoalRepository).save(captor.capture());

        UserGoal savedUserGoal = captor.getValue();

        assertEquals(userId, savedUserGoal.getUser().getUserId());
        assertEquals(goalMasterId, savedUserGoal.getGoalMaster().getGoalMasterId());
        assertEquals(0, savedUserGoal.getCurrentValue());
        assertEquals(UserGoalStatus.ACTIVE, savedUserGoal.getStatus());
        assertEquals(savedUserGoal.getStartDate(), savedUserGoal.getEndDate());
        assertNull(savedUserGoal.getCompletedAt());
    }

    @Test
    @DisplayName("존재하지 않는 목표는 선택할 수 없다")
    void 존재하지_않는_목표는_선택할_수_없다() {
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(userRepository).findById(userId);
        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository, never()).existsByUser_UserIdAndGoalMaster_GoalMasterId(any(), any());
        verify(userGoalRepository, never()).countByUser_UserId(any());
        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

    @Test
    @DisplayName("이미 선택한 목표는 중복 선택할 수 없다")
    void 이미_선택한_목표는_중복_선택할_수_없다() {
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(true);

        assertThrows(GoalAlreadySelectedException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(userRepository).findById(userId);
        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository, never()).countByUser_UserId(any());
        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

    @Test
    @DisplayName("학습 목표는 최대 3개까지만 선택할 수 있다")
    void 학습_목표는_최대_3개까지만_선택할_수_있다() {
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(false);

        given(userGoalRepository.countByUser_UserId(userId))
                .willReturn(3);

        assertThrows(GoalSelectionLimitExceededException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(userRepository).findById(userId);
        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository).countByUser_UserId(userId);
        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

    @Test
    @DisplayName("진행 중인 목표를 정상적으로 해제한다")
    void 진행_중인_목표를_정상적으로_해제한다() {
        Long userGoalId = 10L;

        UserGoal userGoal = UserGoal.of(
                userGoalId,
                user,
                goalMaster,
                0,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        given(userGoalRepository.findById(userGoalId))
                .willReturn(Optional.of(userGoal));

        learningGoalService.cancelGoal(userGoalId);

        verify(userGoalRepository).findById(userGoalId);
        assertEquals(UserGoalStatus.CANCELED, userGoal.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 목표는 해제할 수 없다")
    void 존재하지_않는_사용자_목표는_해제할_수_없다() {
        Long userGoalId = 999L;

        given(userGoalRepository.findById(userGoalId))
                .willReturn(Optional.empty());

        assertThrows(UserGoalNotFoundException.class,
                () -> learningGoalService.cancelGoal(userGoalId));

        verify(userGoalRepository).findById(userGoalId);
    }

    @Test
    @DisplayName("사용자의 진행 중인 목표 목록을 조회한다")
    void 사용자의_진행_중인_목표_목록을_조회한다() {
        UserGoal activeGoal1 = UserGoal.of(
                1L,
                user,
                goalMaster,
                0,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        UserGoal activeGoal2 = UserGoal.of(
                2L,
                user,
                goalMaster,
                0,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        given(userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE))
                .willReturn(List.of(activeGoal1, activeGoal2));

        List<UserGoal> result = learningGoalService.getActiveGoals(userId);

        verify(userGoalRepository).findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);
        assertEquals(2, result.size());
        assertEquals(UserGoalStatus.ACTIVE, result.get(0).getStatus());
        assertEquals(UserGoalStatus.ACTIVE, result.get(1).getStatus());
    }

    @Test
    @DisplayName("진행 중인 목표의 진행도를 정상적으로 증가시킨다")
    void 진행중인_목표의_진행도를_정상적으로_증가시킨다() {
        GoalMaster progressGoalMaster = GoalMaster.of(
                goalMasterId,
                "하루 3회 학습",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        UserGoal userGoal = UserGoal.of(
                1L,
                user,
                progressGoalMaster,
                1,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        given(userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE))
                .willReturn(List.of(userGoal));

        learningGoalService.updateGoalProgress(userId, GoalType.STUDY_COUNT, 1);

        verify(userGoalRepository).findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);
        assertEquals(2, userGoal.getCurrentValue());
        assertEquals(UserGoalStatus.ACTIVE, userGoal.getStatus());
    }

    @Test
    @DisplayName("목표 진행도가 목표값에 도달하면 완료 처리한다")
    void 목표_진행도가_목표값에_도달하면_완료_처리한다() {
        GoalMaster progressGoalMaster = GoalMaster.of(
                goalMasterId,
                "하루 3회 학습",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        UserGoal userGoal = UserGoal.of(
                1L,
                user,
                progressGoalMaster,
                2,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        given(userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE))
                .willReturn(List.of(userGoal));

        learningGoalService.updateGoalProgress(userId, GoalType.STUDY_COUNT, 1);

        verify(userGoalRepository).findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);
        assertEquals(3, userGoal.getCurrentValue());
        assertEquals(UserGoalStatus.COMPLETED, userGoal.getStatus());
    }

    @Test
    @DisplayName("사용자의 완료된 목표 목록을 조회한다")
    void 사용자의_완료된_목표_목록을_조회한다() {
        UserGoal completedGoal1 = UserGoal.of(
                1L,
                user,
                goalMaster,
                1,
                UserGoalStatus.COMPLETED,
                LocalDate.now(),
                LocalDate.now()
        );

        UserGoal completedGoal2 = UserGoal.of(
                2L,
                user,
                goalMaster,
                1,
                UserGoalStatus.COMPLETED,
                LocalDate.now(),
                LocalDate.now()
        );

        given(userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.COMPLETED))
                .willReturn(List.of(completedGoal1, completedGoal2));

        List<UserGoal> result = learningGoalService.getCompletedGoals(userId);

        verify(userGoalRepository).findAllByUser_UserIdAndStatus(userId, UserGoalStatus.COMPLETED);
        assertEquals(2, result.size());
        assertEquals(UserGoalStatus.COMPLETED, result.get(0).getStatus());
        assertEquals(UserGoalStatus.COMPLETED, result.get(1).getStatus());
    }

    @Test
    @DisplayName("사용자가 아직 선택하지 않은 목표 목록을 조회한다")
    void 사용자가_아직_선택하지_않은_목표_목록을_조회한다() {
        GoalMaster goal1 = GoalMaster.of(1L, "하루 1회 학습", GoalPeriodType.DAILY, GoalType.STUDY_COUNT, 1);
        GoalMaster goal2 = GoalMaster.of(2L, "하루 3회 학습", GoalPeriodType.DAILY, GoalType.STUDY_COUNT, 3);
        GoalMaster goal3 = GoalMaster.of(3L, "주간 말하기 5회", GoalPeriodType.WEEKLY, GoalType.SPEAKING_COUNT, 5);

        UserGoal selectedGoal = UserGoal.of(
                10L,
                user,
                goal1,
                0,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        given(goalMasterRepository.findAllByIsActiveTrue())
                .willReturn(List.of(goal1, goal2, goal3));

        given(userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE))
                .willReturn(List.of(selectedGoal));

        List<GoalMaster> result = learningGoalService.getAvailableGoals(userId);

        verify(goalMasterRepository).findAllByIsActiveTrue();
        verify(userGoalRepository).findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getGoalMasterId());
        assertEquals(3L, result.get(1).getGoalMasterId());
    }
}