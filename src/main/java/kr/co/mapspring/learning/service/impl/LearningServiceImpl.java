package kr.co.mapspring.learning.service.impl;

import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.repository.StudyScoreRepository;
import kr.co.mapspring.learning.service.LearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningServiceImpl implements LearningService {

    private final StudyLogRepository studyLogRepository;
    private final StudyScoreRepository studyScoreRepository;

    @Override
    @Transactional
    public void recordStudyLog(Long userId, Long sessionId, StudyType studyType, Integer earnedExp, Integer naturalnessScore, Integer fluencyScore, Integer totalScore) {

        StudyLog studyLog = StudyLog.create(
                userId,
                sessionId,
                studyType,
                earnedExp
        );

        StudyLog savedStudyLog = studyLogRepository.save(studyLog);

        StudyScore studyScore = StudyScore.create(
                savedStudyLog,
                naturalnessScore,
                fluencyScore,
                totalScore
        );

        studyScoreRepository.save(studyScore);
    }
}
