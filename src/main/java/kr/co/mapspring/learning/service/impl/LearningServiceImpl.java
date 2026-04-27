package kr.co.mapspring.learning.service.impl;

import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.learning.dto.LearningLogDto;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.repository.StudyScoreRepository;
import kr.co.mapspring.learning.service.LearningGoalService;
import kr.co.mapspring.learning.service.LearningService;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningServiceImpl implements LearningService {

    private final StudyLogRepository studyLogRepository;
    private final StudyScoreRepository studyScoreRepository;
    private final LearningGoalService learningGoalService;
    private final UserRepository userRepository;
    private final LearningSessionRepository learningSessionRepository;

    @Override
    @Transactional
    public void recordStudyLog(Long userId, Long sessionId, StudyType studyType, Integer earnedExp, Integer naturalnessScore, Integer fluencyScore, Integer totalScore) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

//      TODO: 추후 공통 예외처리 예정
        LearningSession session = learningSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션 없음"));

        StudyLog studyLog = StudyLog.create(
                user,
                session,
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

        learningGoalService.updateGoalProgress(userId, GoalType.STUDY_COUNT, 1);
    }

    @Override
    public List<LearningLogDto.ResponseLog> getStudyLogs(Long userId) {
        List<StudyScore> studyScores = studyScoreRepository.findAllByStudyLog_User_UserId(userId);

        return studyScores.stream()
                .map(studyScore -> LearningLogDto.ResponseLog.from(
                        studyScore.getStudyLog(),
                        studyScore
                ))
                .toList();
    }
}
