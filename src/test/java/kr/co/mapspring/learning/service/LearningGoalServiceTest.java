package kr.co.mapspring.learning.service;

import kr.co.mapspring.global.exception.learning.DuplicateUserGoalException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.impl.LearningGoalServiceImpl;
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


    @Test
    void 학습_목표를_정상적으로_선택한다() {

        Long userId = 1L;
        Long goalMasterId = 100L;

        GoalMaster goalMaster = GoalMaster.builder()
                .goalMasterId(goalMasterId)
                .goalTitle(("하루 1회 학습"))
                .build();

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMasterId(userId, goalMasterId))
                .willReturn(false);

        learningGoalService.selectGoal(userId, goalMasterId);

        verify(userGoalRepository).save(any(UserGoal.class));

    }

    @Test
    void 존재하지_않는_목표는_선택할_수_없다() {

        Long userId = 1L;
        Long goalMasterId = 100L;

        GoalMaster goalMaster = GoalMaster.builder()
                .goalMasterId(goalMasterId)
                .goalTitle("하루 1회 학습")
                .build();

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));

    }

    @Test
    void 이미_선택한_목표는_중복_선택할_수_없다() {

        Long userId = 1L;
        Long goalMasterId = 100L;

        GoalMaster goalMaster = GoalMaster.builder()
                .goalMasterId(goalMasterId)
                .goalTitle("하루 1회 학습")
                .build();

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        given(userGoalRepository.existsByUserIdAndGoalMasterId(userId, goalMasterId))
                .willReturn(true);

        assertThrows(DuplicateUserGoalException.class,
                () -> learningGoalService.selectGoal(userId, goalMasterId));


    }

}
