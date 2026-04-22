package kr.co.mapspring.learning.service;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
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

import java.time.Period;
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
                GoalPeriodType.DAILY
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
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(false);

        given(userGoalRepository.countByUserId(userId))
                .willReturn(3);

        assertThrows(GoalSelectionLimitExceededException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

        verify(goalMasterRepository).findById(goalMasterId);
        verify(userGoalRepository).existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);
        verify(userGoalRepository).countByUserId(userId);

        verify(userGoalRepository, never()).save(any(UserGoal.class));
    }

}
