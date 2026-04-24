package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.repository.StudyScoreRepository;
import kr.co.mapspring.learning.service.impl.LearningServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LearningServiceTest {

    @Mock
    private StudyLogRepository studyLogRepository;

    @Mock
    private StudyScoreRepository studyScoreRepository;

    @InjectMocks
    private LearningServiceImpl learningService;

    @Test
    @DisplayName("학습 기록과 점수를 정상적으로 저장한다")
    void 학습_기록과_점수를_정상적으로_저장한다() {

        Long userId = 1L;
        Long sessionId = 10L;
        StudyType studyType = StudyType.SCENARIO;
        Integer earnedExp = 20;

        Integer naturalnessScore = 80;
        Integer fluencyScore = 70;
        Integer totalScore = 75;

        given(studyLogRepository.save(any(StudyLog.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        learningService.recordStudyLog(
                userId,
                sessionId,
                studyType,
                earnedExp,
                naturalnessScore,
                fluencyScore,
                totalScore);

        ArgumentCaptor<StudyLog> studyLogCaptor = ArgumentCaptor.forClass(StudyLog.class);
        ArgumentCaptor<StudyScore> studyScoreCaptor = ArgumentCaptor.forClass(StudyScore.class);

        verify(studyLogRepository).save(studyLogCaptor.capture());
        verify(studyScoreRepository).save(studyScoreCaptor.capture());

        StudyLog savedStudyLog = studyLogCaptor.getValue();
        StudyScore savedStudyScore = studyScoreCaptor.getValue();

        assertEquals(userId, savedStudyLog.getUserId());
        assertEquals(sessionId, savedStudyLog.getSessionId());
        assertEquals(studyType, savedStudyLog.getStudyType());
        assertEquals(earnedExp, savedStudyLog.getEarnedExp());

        assertEquals(savedStudyLog, savedStudyScore.getStudyLog());
        assertEquals(naturalnessScore, savedStudyScore.getNaturalnessScore());
        assertEquals(fluencyScore, savedStudyScore.getFluencyScore());
        assertEquals(totalScore, savedStudyScore.getTotalScore());
    }

}
