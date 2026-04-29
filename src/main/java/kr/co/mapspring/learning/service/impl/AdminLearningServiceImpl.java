package kr.co.mapspring.learning.service.impl;


import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.learning.dto.AdminLearningDto;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.service.AdminLearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminLearningServiceImpl implements AdminLearningService {

    private final StudyLogRepository studyLogRepository;
    private final GoalMasterRepository goalMasterRepository;

    @Override
    public List<StudyLog> getStudyLogs() {
        return studyLogRepository.findAll();
    }

    @Override
    public List<StudyLog> getStudyLogsByUserId(Long userId) {
        return studyLogRepository.findAllByUser_UserId(userId);
    }

    @Override
    public List<GoalMaster> getGoalMasters() {
        return goalMasterRepository.findAll();
    }

    @Override
    @Transactional
    public void updateGoalActive(Long goalMasterId, boolean active) {
        GoalMaster goalMaster = goalMasterRepository.findById(goalMasterId)
                .orElseThrow(GoalMasterNotFoundException::new);

        goalMaster.updateActive(active);
    }

    @Override
    @Transactional
    public void createGoal(AdminLearningDto.RequestCreateGoal request) {
        GoalMaster goalMaster = GoalMaster.create(
                request.getBadgeId(),
                request.getGoalType(),
                request.getGoalTitle(),
                request.getGoalDescription(),
                request.getTargetValue(),
                request.getPeriodType()
        );

        goalMasterRepository.save(goalMaster);
    }

    @Override
    @Transactional
    public void updateGoal(Long goalMasterId, AdminLearningDto.RequestUpdateGoal request) {
        GoalMaster goalMaster = goalMasterRepository.findById(goalMasterId)
                .orElseThrow(GoalMasterNotFoundException::new);

        goalMaster.update(
                request.getBadgeId(),
                request.getGoalType(),
                request.getGoalTitle(),
                request.getGoalDescription(),
                request.getTargetValue(),
                request.getPeriodType()
        );
    }

    @Override
    @Transactional
    public void deleteGoal(Long goalMasterId) {
        GoalMaster goalMaster = goalMasterRepository.findById(goalMasterId)
                .orElseThrow(GoalMasterNotFoundException::new);

        goalMaster.updateActive(false);
    }
}
