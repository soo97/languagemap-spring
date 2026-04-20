package kr.co.mapspring.learning.service;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.impl.LearningGoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LearningGoalServiceTest {

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

        goalMaster = GoalMaster.builder()
                .goalMasterId(goalMasterId)
                .goalTitle("하루 1회 학습")
                .build();
    }

    @Test
    @DisplayName("학습 목표를 정상적으로 선택한다")
    void 학습_목표를_정상적으로_선택한다() {
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(false);

        given(userGoalRepository.countByUserId(userId))
                .willReturn(2);

        learningGoalService.selectGoal(userId, goalMasterId);

        verify(userGoalRepository).save(any(UserGoal.class));

    }

    @Test
    @DisplayName("존재하지 않는 목표는 선택할 수 없다")
    void 존재하지_않는_목표는_선택할_수_없다() {
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));
    }

    @Test
    @DisplayName("이미 선택한 목표는 중복 선택할 수 없다")
    void 이미_선택한_목표는_중복_선택할_수_없다() {
        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId))
                .willReturn(true);

        assertThrows(GoalAlreadySelectedException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));
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
    }

}
