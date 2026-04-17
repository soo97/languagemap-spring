package kr.co.mapspring.learning.service.impl;

import kr.co.mapspring.global.exception.learning.DuplicateUserGoalException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.UserGoalRepository;
import kr.co.mapspring.learning.service.LearningGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LearningGoalServiceImpl implements LearningGoalService {

    private final GoalMasterRepository goalMasterRepository;
    private final UserGoalRepository userGoalRepository;

    public void selectGoal(Long userId, Long goalMasterId) {

        // 1. 목표 존재 확인
        goalMasterRepository.findById(goalMasterId)
                .orElseThrow(() -> new GoalMasterNotFoundException("존재하지 않는 목표입니다."));

        // 2. 중복 체크
        boolean exists = userGoalRepository.existsByUserIdAndGoalMasterId(userId, goalMasterId);
        if (exists) {
            throw new DuplicateUserGoalException("이미 선택한 목표입니다.");
        }

        // 3. 저장
        UserGoal userGoal = UserGoal.builder()
                .userId(userId)
                .goalMasterId(goalMasterId)
                .build();

        userGoalRepository.save(userGoal);


    }
}
