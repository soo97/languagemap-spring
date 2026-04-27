package kr.co.mapspring.learning.service.impl;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.global.exception.learning.UserGoalNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.UserGoalStatus;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.LearningGoalService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningGoalServiceImpl implements LearningGoalService {
    private static final int MAX_GOAL_COUNT = 3;

    private final GoalMasterRepository goalMasterRepository;
    private final UserGoalRepository userGoalRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void selectGoal(Long userId, Long goalMasterId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        GoalMaster goalMaster = goalMasterRepository.findById(goalMasterId)
                .orElseThrow(GoalMasterNotFoundException::new);

        boolean alreadySelected =
                userGoalRepository.existsByUser_UserIdAndGoalMaster_GoalMasterId(userId, goalMasterId);

        if (alreadySelected) {
            throw new GoalAlreadySelectedException();
        }

        int selectedCount = userGoalRepository.countByUser_UserId(userId);

        if (selectedCount > MAX_GOAL_COUNT) {
            throw new GoalSelectionLimitExceededException();
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(startDate, goalMaster);

        UserGoal userGoal = UserGoal.create(user, goalMaster, startDate, endDate);

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
        return userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);
    }

    private LocalDate calculateEndDate(LocalDate startDate, GoalMaster goalMaster) {
        return switch (goalMaster.getPeriodType()) {
            case DAILY -> startDate;
            case WEEKLY -> startDate.plusWeeks(1);
            case MONTHLY -> startDate.plusMonths(1);
            case NONE -> null;
        };
    }

    @Override
    @Transactional
    public void updateGoalProgress(Long userId, GoalType goalType, Integer amount) {
        List<UserGoal> activeGoals = userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);

        for (UserGoal userGoal : activeGoals) {
            if (userGoal.getGoalMaster().getGoalType() != goalType) {
                continue;
            }

            int updatedValue = userGoal.getCurrentValue() + amount;
            userGoal.updateCurrentValue(updatedValue);

            if (updatedValue >= userGoal.getGoalMaster().getTargetValue()) {
                userGoal.complete();
            }
        }
    }

    @Override
    public List<UserGoal> getCompletedGoals(Long userId) {
        return userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.COMPLETED);
    }

    @Override
    public List<GoalMaster> getAvailableGoals(Long userId) {
        List<GoalMaster> activeGoalMasters = goalMasterRepository.findAllByIsActiveTrue();
        List<UserGoal> activeUserGoals = userGoalRepository.findAllByUser_UserIdAndStatus(userId, UserGoalStatus.ACTIVE);

        Set<Long> selectedGoalIds = activeUserGoals.stream()
                .map(userGoal -> userGoal.getGoalMaster().getGoalMasterId())
                .collect(Collectors.toSet());

        return activeGoalMasters.stream()
                .filter(goalMaster -> !selectedGoalIds.contains(goalMaster.getGoalMasterId()))
                .toList();
    }
}
