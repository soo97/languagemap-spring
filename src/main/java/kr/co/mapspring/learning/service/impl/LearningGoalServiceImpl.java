package kr.co.mapspring.learning.service.impl;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.global.exception.learning.UserGoalNotFoundException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.UserGoalStatus;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.LearningGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningGoalServiceImpl implements LearningGoalService {
    private static final int MAX_GOAL_COUNT = 3;

    private final GoalMasterRepository goalMasterRepository;
    private final UserGoalRepository userGoalRepository;

    @Override
    @Transactional
    public void selectGoal(Long userId, Long goalMasterId) {
        GoalMaster goalMaster = goalMasterRepository.findById(goalMasterId)
                .orElseThrow(GoalMasterNotFoundException::new);

        boolean alreadySelected =
                userGoalRepository.existsByUserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);

        if (alreadySelected) {
            throw new GoalAlreadySelectedException();
        }

        int selectedCount = userGoalRepository.countByUserId(userId);

        if (selectedCount >= MAX_GOAL_COUNT) {
            throw new GoalSelectionLimitExceededException();
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calcultateEndDate(startDate, goalMaster);

        UserGoal userGoal = UserGoal.of(userId, goalMaster, startDate, endDate);

        userGoalRepository.save(userGoal);
    }

    @Override
    @Transactional
    public void cancelGoal(Long userGoalId) {
        UserGoal userGoal = userGoalRepository.findById(userGoalId)
                .orElseThrow(UserGoalNotFoundException::new);

        userGoal.cancel();
    }

    @Override
    public List<UserGoal> getActiveGoals(Long userId) {
        return userGoalRepository.findAllByUserIdAndStatus(userId, UserGoalStatus.ACTIVE);
    }

    private LocalDate calcultateEndDate(LocalDate startDate, GoalMaster goalMaster) {
        return switch (goalMaster.getPeriodType()) {
            case DAILY -> startDate;
            case WEEKLY -> startDate.plusWeeks(1);
            case MONTHLY -> startDate.plusMonths(1);
            case NONE -> null;
        };
    }
}
