package kr.co.mapspring.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.ai.dto.CoachingFeedbackDto;
import kr.co.mapspring.ai.entity.CoachingFeedback;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingFeedbackRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.impl.CoachingFeedbackServiceImpl;
import kr.co.mapspring.global.exception.ai.CoachingFeedbackAlreadyExistsException;
import kr.co.mapspring.global.exception.ai.CoachingFeedbackNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;

@ExtendWith(MockitoExtension.class)
class CoachingFeedbackServiceTest {

    @InjectMocks
    private CoachingFeedbackServiceImpl coachingFeedbackService;

    @Mock
    private CoachingFeedbackRepository coachingFeedbackRepository;

    @Mock
    private CoachingSessionRepository coachingSessionRepository;

    private CoachingSession coachingSession;
    private CoachingFeedback coachingFeedback;

    @BeforeEach
    void setUp() throws Exception {
        LearningSession learningSession = createInstance(LearningSession.class);
        setField(learningSession, "sessionId", 10L);

        coachingSession = createInstance(CoachingSession.class);
        setField(coachingSession, "coachingSessionId", 100L);
        setField(coachingSession, "learningSession", learningSession);
        setField(coachingSession, "coachingSessionStatus", CoachingSessionStatus.RUNNING);
        setField(coachingSession, "selectedOption", "WORD");
        setField(coachingSession, "currentTurnOrder", 3);

        coachingFeedback = createInstance(CoachingFeedback.class);
        setField(coachingFeedback, "coachingFeedbackId", 1L);
        setField(coachingFeedback, "coachingSession", coachingSession);
        setField(coachingFeedback, "totalScore", 87);
        setField(coachingFeedback, "summaryFeedback", "전체적으로 자연스럽게 답변했습니다.");
        setField(coachingFeedback, "naturalnessLevel", "GOOD");
        setField(coachingFeedback, "naturalnessComment", "문장이 자연스럽게 이어졌습니다.");
        setField(coachingFeedback, "flowLevel", "GOOD");
        setField(coachingFeedback, "flowComment", "질문에 맞게 적절히 답변했습니다.");
        setField(coachingFeedback, "pronunciationLevel", "CHECK");
        setField(coachingFeedback, "pronunciationComment", "almond 발음을 더 연습하면 좋습니다.");
        setField(coachingFeedback, "problemWords", "[\"almond\"]");
        setField(coachingFeedback, "createdAt", LocalDateTime.of(2026, 5, 3, 10, 0));
    }

    @Test
    @DisplayName("코칭 최종 피드백 저장 성공")
    void 코칭_최종_피드백_저장_성공() {
        CoachingFeedbackDto.RequestSaveCoachingFeedback request =
                CoachingFeedbackDto.RequestSaveCoachingFeedback.builder()
                        .coachingSessionId(100L)
                        .totalScore(87)
                        .summaryFeedback("전체적으로 자연스럽게 답변했습니다.")
                        .naturalnessLevel("GOOD")
                        .naturalnessComment("문장이 자연스럽게 이어졌습니다.")
                        .flowLevel("GOOD")
                        .flowComment("질문에 맞게 적절히 답변했습니다.")
                        .pronunciationLevel("CHECK")
                        .pronunciationComment("almond 발음을 더 연습하면 좋습니다.")
                        .problemWords("[\"almond\"]")
                        .build();

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingFeedbackRepository.existsByCoachingSession_CoachingSessionId(100L))
                .thenReturn(false);
        when(coachingFeedbackRepository.save(any(CoachingFeedback.class)))
                .thenReturn(coachingFeedback);

        CoachingFeedbackDto.ResponseCoachingFeedback response =
                coachingFeedbackService.saveCoachingFeedback(request);

        assertThat(response.getCoachingFeedbackId()).isEqualTo(1L);
        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getTotalScore()).isEqualTo(87);
        assertThat(response.getNaturalnessLevel()).isEqualTo("GOOD");
        assertThat(response.getFlowLevel()).isEqualTo("GOOD");
        assertThat(response.getPronunciationLevel()).isEqualTo("CHECK");
        assertThat(response.getProblemWords()).isEqualTo("[\"almond\"]");

        verify(coachingFeedbackRepository).save(any(CoachingFeedback.class));
    }

    @Test
    @DisplayName("코칭 최종 피드백 저장 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_최종_피드백_저장_실패_세션없음() {
        CoachingFeedbackDto.RequestSaveCoachingFeedback request =
                CoachingFeedbackDto.RequestSaveCoachingFeedback.builder()
                        .coachingSessionId(999L)
                        .totalScore(87)
                        .build();

        when(coachingSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingFeedbackService.saveCoachingFeedback(request))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(coachingFeedbackRepository, never()).save(any(CoachingFeedback.class));
    }

    @Test
    @DisplayName("코칭 최종 피드백 저장 실패 - 이미 최종 피드백이 존재한다")
    void 코칭_최종_피드백_저장_실패_이미존재() {
        CoachingFeedbackDto.RequestSaveCoachingFeedback request =
                CoachingFeedbackDto.RequestSaveCoachingFeedback.builder()
                        .coachingSessionId(100L)
                        .totalScore(87)
                        .build();

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingFeedbackRepository.existsByCoachingSession_CoachingSessionId(100L))
                .thenReturn(true);

        assertThatThrownBy(() -> coachingFeedbackService.saveCoachingFeedback(request))
                .isInstanceOf(CoachingFeedbackAlreadyExistsException.class)
                .hasMessage("이미 코칭 최종 피드백이 존재합니다.");

        verify(coachingFeedbackRepository, never()).save(any(CoachingFeedback.class));
    }

    @Test
    @DisplayName("코칭 세션 ID 기준 최종 피드백 조회 성공")
    void 코칭_세션_ID_기준_최종_피드백_조회_성공() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingFeedbackRepository.findByCoachingSession_CoachingSessionId(100L))
                .thenReturn(Optional.of(coachingFeedback));

        CoachingFeedbackDto.ResponseCoachingFeedback response =
                coachingFeedbackService.getCoachingFeedback(100L);

        assertThat(response.getCoachingFeedbackId()).isEqualTo(1L);
        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getTotalScore()).isEqualTo(87);
        assertThat(response.getSummaryFeedback()).isEqualTo("전체적으로 자연스럽게 답변했습니다.");
    }

    @Test
    @DisplayName("코칭 세션 ID 기준 최종 피드백 조회 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_세션_ID_기준_최종_피드백_조회_실패_세션없음() {
        when(coachingSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingFeedbackService.getCoachingFeedback(999L))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(coachingFeedbackRepository, never())
                .findByCoachingSession_CoachingSessionId(999L);
    }

    @Test
    @DisplayName("코칭 세션 ID 기준 최종 피드백 조회 실패 - 최종 피드백이 존재하지 않는다")
    void 코칭_세션_ID_기준_최종_피드백_조회_실패_피드백없음() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingFeedbackRepository.findByCoachingSession_CoachingSessionId(100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingFeedbackService.getCoachingFeedback(100L))
                .isInstanceOf(CoachingFeedbackNotFoundException.class)
                .hasMessage("코칭 최종 피드백을 찾을 수 없습니다.");
    }

    private <T> T createInstance(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;

        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }

        throw new NoSuchFieldException(fieldName);
    }
}