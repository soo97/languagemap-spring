package kr.co.mapspring.learning.service.impl;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.LearningGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        UserGoal userGoal = UserGoal.createForTest(userId, goalMaster);

        userGoalRepository.save(userGoal);


    }
}
