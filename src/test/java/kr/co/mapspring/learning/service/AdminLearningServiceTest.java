package kr.co.mapspring.learning.service;

import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.repository.StudyScoreRepository;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminLearningServiceTest {

    @Mock
    private StudyLogRepository studyLogRepository;

    @Mock
    private StudyScoreRepository studyScoreRepository;

    @Mock
    private GoalMasterRepository goalMasterRepository;

    @InjectMocks
    private AdminLearningServiceImpl adminLearningService;

    @Test
    @DisplayName("관리자는 전체 학습 기록을 조회한다")
    void 관리자는_전체_학습_기록을_조회한다() {

        User user = mock(User.class);
        given(user.getUserId()).willReturn(1L);

        LearningSession session = mock(LearningSession.class);

        StudyLog studyLog = StudyLog.of(
                1L,
                user,
                session,
                StudyType.SCENARIO,
                20
        );

        StudyScore studyScore = StudyScore.of(
                1L,
                studyLog,
                80,
                70,
                75
        );

        given(studyLogRepository.findAll())
                .willReturn(List.of(studyLog));

        given(studyScoreRepository.findByStudyLog_StudyLogId(1L))
                .willReturn(Optional.of(studyScore));

        List<AdminLearningDto.ResponseStudyLog> result =
                adminLearningService.getStudyLogs();

        verify(studyLogRepository).findAll();
        verify(studyScoreRepository).findByStudyLog_StudyLogId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudyLogId());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(StudyType.SCENARIO, result.get(0).getStudyType());
        assertEquals(20, result.get(0).getEarnedExp());
        assertEquals(75, result.get(0).getTotalScore());
    }

    @Test
    @DisplayName("관리자는 특정 사용자의 학습 기록을 조회한다")
    void 관리자는_특정_사용자의_학습_기록을_조회한다() {

        Long userId = 1L;

        User user = mock(User.class);
        given(user.getUserId()).willReturn(userId);

        LearningSession session = mock(LearningSession.class);

        StudyLog studyLog = StudyLog.of(
                1L,
                user,
                session,
                StudyType.SPEAKING,
                30
        );

        StudyScore studyScore = StudyScore.of(
                1L,
                studyLog,
                90,
                80,
                85
        );

        given(studyLogRepository.findAllByUser_UserId(userId))
                .willReturn(List.of(studyLog));

        given(studyScoreRepository.findByStudyLog_StudyLogId(1L))
                .willReturn(Optional.of(studyScore));

        List<AdminLearningDto.ResponseStudyLog> result =
                adminLearningService.getStudyLogsByUserId(userId);

        verify(studyLogRepository).findAllByUser_UserId(userId);
        verify(studyScoreRepository).findByStudyLog_StudyLogId(1L);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals(StudyType.SPEAKING, result.get(0).getStudyType());
        assertEquals(85, result.get(0).getTotalScore());
    }

    @Test
    @DisplayName("관리자는 학습 목표 목록을 조회한다")
    void 관리자는_학습_목표_목록을_조회한다() {

        GoalMaster goal1 = GoalMaster.of(
                1L,
                "하루 학습 3회",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        GoalMaster goal2 = GoalMaster.of(
                2L,
                "하루 학습 30분",
                GoalPeriodType.DAILY,
                GoalType.STUDY_TIME,
                30
        );

        given(goalMasterRepository.findAll())
                .willReturn(List.of(goal1, goal2));

        List<AdminLearningDto.ResponseGoalMaster> result =
                adminLearningService.getGoalMasters();

        verify(goalMasterRepository).findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getGoalMasterId());
        assertEquals("하루 학습 3회", result.get(0).getGoalTitle());
        assertEquals(GoalType.STUDY_COUNT, result.get(0).getGoalType());
        assertTrue(result.get(0).isActive());
    }

    @Test
    @DisplayName("관리자는 목표를 활성화한다")
    void 관리자는_목표를_활성화한다() {

        Long goalMasterId = 1L;

        GoalMaster goalMaster = GoalMaster.of(
                goalMasterId,
                "하루 학습 3회",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        goalMaster.updateActive(false);

        AdminLearningDto.RequestUpdateGoalActive request =
                new AdminLearningDto.RequestUpdateGoalActive(true);

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        adminLearningService.updateGoalActive(goalMasterId, request);

        verify(goalMasterRepository).findById(goalMasterId);

        assertTrue(goalMaster.isActive());
    }

    @Test
    @DisplayName("관리자는 목표를 비활성화한다")
    void 관리자는_목표를_비활성화한다() {

        Long goalMasterId = 1L;

        GoalMaster goalMaster = GoalMaster.of(
                goalMasterId,
                "하루 학습 3회",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        AdminLearningDto.RequestUpdateGoalActive request =
                new AdminLearningDto.RequestUpdateGoalActive(false);

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        adminLearningService.updateGoalActive(goalMasterId, request);

        verify(goalMasterRepository).findById(goalMasterId);

        assertFalse(goalMaster.isActive());
    }

    @Test
    @DisplayName("존재하지 않는 목표 상태 변경 시 예외가 발생한다")
    void 존재하지_않는_목표_상태_변경시_예외가_발생한다() {

        Long goalMasterId = 999L;

        AdminLearningDto.RequestUpdateGoalActive request =
                new AdminLearningDto.RequestUpdateGoalActive(false);

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> adminLearningService.updateGoalActive(goalMasterId, request));

        verify(goalMasterRepository).findById(goalMasterId);
    }
    }
}
