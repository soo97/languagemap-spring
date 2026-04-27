package kr.co.mapspring.learning.service;

import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.repository.StudyScoreRepository;
import kr.co.mapspring.learning.service.impl.LearningServiceImpl;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import kr.co.mapspring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LearningServiceTest {

    @Mock
    private StudyLogRepository studyLogRepository;

    @Mock
    private StudyScoreRepository studyScoreRepository;

    @Mock
    private LearningGoalService learningGoalService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LearningSessionRepository learningSessionRepository;

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

        User user = User.builder()
                .userId(userId)
                .email("test@test.com")
                .name("테스트")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("서울")
                .phoneNumber("010-1234-5678")
                .passwordHash("1234")
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        LearningSession session = mock(LearningSession.class);

        ReflectionTestUtils.setField(session, "sessionId", sessionId);
        ReflectionTestUtils.setField(session, "startTime", LocalDateTime.now());
        ReflectionTestUtils.setField(session, "user", user);

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(learningSessionRepository.findById(sessionId))
                .willReturn(Optional.of(session));

        given(studyLogRepository.save(any(StudyLog.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        learningService.recordStudyLog(
                userId,
                sessionId,
                studyType,
                earnedExp,
                naturalnessScore,
                fluencyScore,
                totalScore
        );

        ArgumentCaptor<StudyLog> studyLogCaptor = ArgumentCaptor.forClass(StudyLog.class);
        ArgumentCaptor<StudyScore> studyScoreCaptor = ArgumentCaptor.forClass(StudyScore.class);

        verify(userRepository).findById(userId);
        verify(learningSessionRepository).findById(sessionId);
        verify(studyLogRepository).save(studyLogCaptor.capture());
        verify(studyScoreRepository).save(studyScoreCaptor.capture());

        StudyLog savedStudyLog = studyLogCaptor.getValue();
        StudyScore savedStudyScore = studyScoreCaptor.getValue();

        assertEquals(user, savedStudyLog.getUser());
        assertEquals(session, savedStudyLog.getLearningSession());
        assertEquals(studyType, savedStudyLog.getStudyType());
        assertEquals(earnedExp, savedStudyLog.getEarnedExp());

        assertEquals(savedStudyLog, savedStudyScore.getStudyLog());
        assertEquals(naturalnessScore, savedStudyScore.getNaturalnessScore());
        assertEquals(fluencyScore, savedStudyScore.getFluencyScore());
        assertEquals(totalScore, savedStudyScore.getTotalScore());

        verify(learningGoalService).updateGoalProgress(userId, GoalType.STUDY_COUNT, 1);
    }
}