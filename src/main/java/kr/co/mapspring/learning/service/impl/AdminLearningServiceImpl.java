package kr.co.mapspring.learning.service.impl;


import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
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

}
