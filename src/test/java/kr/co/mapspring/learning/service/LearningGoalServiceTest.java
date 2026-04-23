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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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

    @InjectMocks
    private LearningGoalServiceImpl learningGoalService;

    private Long userId;
    private Long goalMasterId;
    private GoalMaster goalMaster;

    @BeforeEach
    void setUp() {
        userId = 1L;
        goalMasterId = 100L;
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
        // given
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(false);

        given(userGoalRepository.countByUserId(userId))
                .willReturn(2);

        // when
        learningGoalService.selectGoal(userId, goalMasterId);

        // then
        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository).countByUserId(userId);

        ArgumentCaptor<UserGoal> captor = ArgumentCaptor.forClass(UserGoal.class);
        verify(userGoalRepository).save(captor.capture());

        UserGoal savedUserGoal = captor.getValue();

        assertEquals(userId, savedUserGoal.getUserId());
        assertEquals(goalMasterId, savedUserGoal.getGoalMaster().getGoalMasterId());
        assertEquals(0, savedUserGoal.getCurrentValue());
        assertEquals(UserGoalStatus.ACTIVE, savedUserGoal.getStatus());
        assertEquals(savedUserGoal.getStartDate(), savedUserGoal.getEndDate());
        assertNull(savedUserGoal.getCompletedAt());
    }

    @Test
    @DisplayName("존재하지 않는 목표는 선택할 수 없다")
    void 존재하지_않는_목표는_선택할_수_없다() {
        // given
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(GoalMasterNotFoundException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository, never()).existsByUserIdAndGoalMaster_GoalMasterId(any(), any());
        verify(userGoalRepository, never()).countByUserId(any());
        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

    @Test
    @DisplayName("이미 선택한 목표는 중복 선택할 수 없다")
    void 이미_선택한_목표는_중복_선택할_수_없다() {
        // given
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(true);

        // when & then
        assertThrows(GoalAlreadySelectedException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository, never()).countByUserId(any());
        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

    @Test
    @DisplayName("학습 목표는 최대 3개까지만 선택할 수 있다")
    void 학습_목표는_최대_3개까지만_선택할_수_있다() {
        // given
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(false);

        given(userGoalRepository.countByUserId(userId))
                .willReturn(4);

        // when & then
        assertThrows(GoalSelectionLimitExceededException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository).countByUserId(userId);
        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

    @Test
    @DisplayName("진행 중인 목표를 정상적으로 해제한다")
    void 진행_중인_목표를_정상적으로_해제한다() {
        // given
        Long userGoalId = 10L;
        UserGoal userGoal = UserGoal.of(
                userGoalId,
                userId,
                goalMaster,
                0,
                UserGoalStatus.ACTIVE,
                java.time.LocalDate.now(),
                java.time.LocalDate.now()
        );

        given(userGoalRepository.findById(userGoalId))
                .willReturn(Optional.of(userGoal));

        // when
        learningGoalService.cancelGoal(userGoalId);

        // then
        verify(userGoalRepository).findById(userGoalId);
        assertEquals(UserGoalStatus.CANCELED, userGoal.getStatus());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 목표는 해제할 수 없다")
    void 존재하지_않는_사용자_목표는_해제할_수_없다() {
        // given
        Long userGoalId = 999L;

        given(userGoalRepository.findById(userGoalId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(UserGoalNotFoundException.class,
                () -> learningGoalService.cancelGoal(userGoalId));

        verify(userGoalRepository).findById(userGoalId);
    }

    @Test
    @DisplayName("사용자의 진행 중인 목표 목록을 조회한다")
    void 사용자의_진행_중인_목표_목록을_조회한다() {
        // given
        UserGoal activeGoal1 = UserGoal.of(
                1L,
                userId,
                goalMaster,
                0,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        UserGoal activeGoal2 = UserGoal.of(
                2L,
                userId,
                goalMaster,
                0,
                UserGoalStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now()
        );

        given(userGoalRepository.findAllByUserIdAndStatus(userId, UserGoalStatus.ACTIVE))
                .willReturn(java.util.List.of(activeGoal1, activeGoal2));

        // when
        java.util.List<UserGoal> result = learningGoalService.getActiveGoals(userId);

        // then
        verify(userGoalRepository).findAllByUserIdAndStatus(userId, UserGoalStatus.ACTIVE);
        assertEquals(2, result.size());
        assertEquals(UserGoalStatus.ACTIVE, result.get(0).getStatus());
        assertEquals(UserGoalStatus.ACTIVE, result.get(1).getStatus());
    }

}
