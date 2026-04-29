package kr.co.mapspring.learning.service;

import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.learning.dto.AdminLearningDto;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.learning.repository.GoalMasterRepository;
import kr.co.mapspring.learning.repository.StudyLogRepository;
import kr.co.mapspring.learning.service.impl.AdminLearningServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminLearningServiceTest {

    @Mock
    private StudyLogRepository studyLogRepository;

    @Mock
    private GoalMasterRepository goalMasterRepository;

    @InjectMocks
    private AdminLearningServiceImpl adminLearningService;

    @Test
    @DisplayName("관리자는 전체 학습 기록을 조회한다")
    void 관리자는_전체_학습_기록을_조회한다() {
        User user = mock(User.class);
        LearningSession session = mock(LearningSession.class);

        StudyLog studyLog = StudyLog.of(
                1L,
                user,
                session,
                StudyType.SCENARIO,
                20
        );

        given(studyLogRepository.findAll())
                .willReturn(List.of(studyLog));

        List<StudyLog> result = adminLearningService.getStudyLogs();

        verify(studyLogRepository).findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudyLogId());
        assertEquals(StudyType.SCENARIO, result.get(0).getStudyType());
        assertEquals(20, result.get(0).getEarnedExp());
    }

    @Test
    @DisplayName("관리자는 특정 사용자의 학습 기록을 조회한다")
    void 관리자는_특정_사용자의_학습_기록을_조회한다() {
        Long userId = 1L;

        User user = mock(User.class);
        LearningSession session = mock(LearningSession.class);

        StudyLog studyLog = StudyLog.of(
                1L,
                user,
                session,
                StudyType.SPEAKING,
                30
        );

        given(studyLogRepository.findAllByUser_UserId(userId))
                .willReturn(List.of(studyLog));

        List<StudyLog> result = adminLearningService.getStudyLogsByUserId(userId);

        verify(studyLogRepository).findAllByUser_UserId(userId);

        assertEquals(1, result.size());
        assertEquals(StudyType.SPEAKING, result.get(0).getStudyType());
        assertEquals(30, result.get(0).getEarnedExp());
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

        List<GoalMaster> result = adminLearningService.getGoalMasters();

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

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        adminLearningService.updateGoalActive(goalMasterId, true);

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

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        adminLearningService.updateGoalActive(goalMasterId, false);

        verify(goalMasterRepository).findById(goalMasterId);

        assertFalse(goalMaster.isActive());
    }

    @Test
    @DisplayName("존재하지 않는 목표 상태 변경 시 예외가 발생한다")
    void 존재하지_않는_목표_상태_변경시_예외가_발생한다() {
        Long goalMasterId = 999L;

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> adminLearningService.updateGoalActive(goalMasterId, false));

        verify(goalMasterRepository).findById(goalMasterId);
    }

    @Test
    @DisplayName("관리자는 학습 목표를 생성한다")
    void 관리자는_학습_목표를_생성한다() {

        AdminLearningDto.RequestCreateGoal request =
                new AdminLearningDto.RequestCreateGoal(
                        1L,
                        GoalType.STUDY_COUNT,
                        "하루 학습 3회",
                        "하루에 학습을 3회 완료합니다.",
                        3,
                        GoalPeriodType.DAILY
                );

        adminLearningService.createGoal(request);

        verify(goalMasterRepository).save(any(GoalMaster.class));
    }

    @Test
    @DisplayName("관리자는 학습 목표를 수정한다")
    void 관리자는_학습_목표를_수정한다() {

        Long goalMasterId = 1L;

        GoalMaster goalMaster = GoalMaster.of(
                goalMasterId,
                "하루 학습 3회",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        AdminLearningDto.RequestUpdateGoal request =
                new AdminLearningDto.RequestUpdateGoal(
                        2L,
                        GoalType.STUDY_TIME,
                        "하루 학습 30분",
                        "하루에 30분 이상 학습합니다.",
                        30,
                        GoalPeriodType.DAILY
                );

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        adminLearningService.updateGoal(goalMasterId, request);

        verify(goalMasterRepository).findById(goalMasterId);

        assertEquals("하루 학습 30분", goalMaster.getGoalTitle());
        assertEquals(GoalType.STUDY_TIME, goalMaster.getGoalType());
        assertEquals(30, goalMaster.getTargetValue());
        assertEquals(GoalPeriodType.DAILY, goalMaster.getPeriodType());
    }

    @Test
    @DisplayName("존재하지 않는 목표 수정 시 예외가 발생한다")
    void 존재하지_않는_목표_수정시_예외가_발생한다() {

        Long goalMasterId = 999L;

        AdminLearningDto.RequestUpdateGoal request =
                new AdminLearningDto.RequestUpdateGoal(
                        1L,
                        GoalType.STUDY_TIME,
                        "하루 학습 30분",
                        "하루에 30분 이상 학습합니다.",
                        30,
                        GoalPeriodType.DAILY
                );

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> adminLearningService.updateGoal(goalMasterId, request));

        verify(goalMasterRepository).findById(goalMasterId);
    }

    @Test
    @DisplayName("관리자는 학습 목표를 삭제한다")
    void 관리자는_학습_목표를_삭제한다() {

        Long goalMasterId = 1L;

        GoalMaster goalMaster = GoalMaster.of(
                goalMasterId,
                "하루 학습 3회",
                GoalPeriodType.DAILY,
                GoalType.STUDY_COUNT,
                3
        );

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.of(goalMaster));

        adminLearningService.deleteGoal(goalMasterId);

        verify(goalMasterRepository).findById(goalMasterId);
        verify(goalMasterRepository).delete(goalMaster);
    }

    @Test
    @DisplayName("존재하지 않는 목표 삭제 시 예외가 발생한다")
    void 존재하지_않는_목표_삭제시_예외가_발생한다() {

        Long goalMasterId = 999L;

        given(goalMasterRepository.findById(goalMasterId))
                .willReturn(Optional.empty());

        assertThrows(GoalMasterNotFoundException.class,
                () -> adminLearningService.deleteGoal(goalMasterId));

        verify(goalMasterRepository).findById(goalMasterId);
    }
}