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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.ai.dto.CoachingPronunciationResultDto;
import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.entity.CoachingPronunciationResult;
import kr.co.mapspring.ai.entity.CoachingScriptTurn;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingMessageRepository;
import kr.co.mapspring.ai.repository.CoachingPronunciationResultRepository;
import kr.co.mapspring.ai.repository.CoachingScriptTurnRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.impl.CoachingPronunciationResultServiceImpl;
import kr.co.mapspring.global.exception.ai.CoachingMessageNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingPronunciationResultNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingScriptTurnNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;

@ExtendWith(MockitoExtension.class)
class CoachingPronunciationResultServiceTest {

    @InjectMocks
    private CoachingPronunciationResultServiceImpl pronunciationResultService;

    @Mock
    private CoachingPronunciationResultRepository pronunciationResultRepository;

    @Mock
    private CoachingMessageRepository coachingMessageRepository;

    @Mock
    private CoachingScriptTurnRepository coachingScriptTurnRepository;

    @Mock
    private CoachingSessionRepository coachingSessionRepository;

    private CoachingSession coachingSession;
    private CoachingScriptTurn scriptTurn;
    private CoachingMessage userMessage;
    private CoachingPronunciationResult pronunciationResult;

    @BeforeEach
    void setUp() throws Exception {
        LearningSession learningSession = createInstance(LearningSession.class);
        setField(learningSession, "sessionId", 10L);

        coachingSession = createInstance(CoachingSession.class);
        setField(coachingSession, "coachingSessionId", 100L);
        setField(coachingSession, "learningSession", learningSession);
        setField(coachingSession, "coachingSessionStatus", CoachingSessionStatus.RUNNING);
        setField(coachingSession, "selectedOption", "WORD");
        setField(coachingSession, "currentTurnOrder", 1);

        scriptTurn = createInstance(CoachingScriptTurn.class);
        setField(scriptTurn, "coachingScriptTurnId", 1L);
        setField(scriptTurn, "coachingSession", coachingSession);
        setField(scriptTurn, "turnOrder", 1);
        setField(scriptTurn, "assistantText", "Good morning. What would you like to order?");
        setField(scriptTurn, "expectedText", "I would like a latte with almond milk, please.");
        setField(scriptTurn, "createdAt", LocalDateTime.of(2026, 5, 3, 10, 0));

        userMessage = createInstance(CoachingMessage.class);
        setField(userMessage, "coachingMessageId", 10L);
        setField(userMessage, "coachingSession", coachingSession);
        setField(userMessage, "coachingScriptTurn", scriptTurn);
        setField(userMessage, "role", CoachingMessageRole.USER);
        setField(userMessage, "message", "I would like a latte with almond milk please.");
        setField(userMessage, "audioUrl", "/static/uploads/user-turn-1.wav");
        setField(userMessage, "createdAt", LocalDateTime.of(2026, 5, 3, 10, 1));

        pronunciationResult = createInstance(CoachingPronunciationResult.class);
        setField(pronunciationResult, "pronunciationResultId", 1000L);
        setField(pronunciationResult, "coachingMessage", userMessage);
        setField(pronunciationResult, "coachingScriptTurn", scriptTurn);
        setField(pronunciationResult, "recognizedText", "I would like a latte with almond milk please.");
        setField(pronunciationResult, "accuracyScore", 96.0);
        setField(pronunciationResult, "fluencyScore", 81.0);
        setField(pronunciationResult, "completenessScore", 100.0);
        setField(pronunciationResult, "pronunciationScore", 87.8);
        setField(pronunciationResult, "feedback", "almond 발음을 조금 더 명확하게 연습하면 좋습니다.");
        setField(pronunciationResult, "problemWords", "[\"almond\"]");
        setField(pronunciationResult, "createdAt", LocalDateTime.of(2026, 5, 3, 10, 2));
    }

    @Test
    @DisplayName("발음 평가 결과 저장 성공")
    void 발음_평가_결과_저장_성공() {
        CoachingPronunciationResultDto.RequestSavePronunciationResult request =
                CoachingPronunciationResultDto.RequestSavePronunciationResult.builder()
                        .coachingMessageId(10L)
                        .coachingScriptTurnId(1L)
                        .recognizedText("I would like a latte with almond milk please.")
                        .accuracyScore(96.0)
                        .fluencyScore(81.0)
                        .completenessScore(100.0)
                        .pronunciationScore(87.8)
                        .feedback("almond 발음을 조금 더 명확하게 연습하면 좋습니다.")
                        .problemWords("[\"almond\"]")
                        .build();

        when(coachingMessageRepository.findById(10L)).thenReturn(Optional.of(userMessage));
        when(coachingScriptTurnRepository.findById(1L)).thenReturn(Optional.of(scriptTurn));
        when(pronunciationResultRepository.save(any(CoachingPronunciationResult.class)))
                .thenReturn(pronunciationResult);

        CoachingPronunciationResultDto.ResponsePronunciationResult response =
                pronunciationResultService.savePronunciationResult(request);

        assertThat(response.getPronunciationResultId()).isEqualTo(1000L);
        assertThat(response.getCoachingMessageId()).isEqualTo(10L);
        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getRecognizedText()).isEqualTo("I would like a latte with almond milk please.");
        assertThat(response.getAccuracyScore()).isEqualTo(96.0);
        assertThat(response.getFluencyScore()).isEqualTo(81.0);
        assertThat(response.getCompletenessScore()).isEqualTo(100.0);
        assertThat(response.getPronunciationScore()).isEqualTo(87.8);
        assertThat(response.getFeedback()).isEqualTo("almond 발음을 조금 더 명확하게 연습하면 좋습니다.");
        assertThat(response.getProblemWords()).isEqualTo("[\"almond\"]");

        verify(pronunciationResultRepository).save(any(CoachingPronunciationResult.class));
    }

    @Test
    @DisplayName("발음 평가 결과 저장 실패 - 코칭 메시지가 존재하지 않는다")
    void 발음_평가_결과_저장_실패_메시지없음() {
        CoachingPronunciationResultDto.RequestSavePronunciationResult request =
                CoachingPronunciationResultDto.RequestSavePronunciationResult.builder()
                        .coachingMessageId(999L)
                        .coachingScriptTurnId(1L)
                        .recognizedText("hello")
                        .build();

        when(coachingMessageRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pronunciationResultService.savePronunciationResult(request))
                .isInstanceOf(CoachingMessageNotFoundException.class)
                .hasMessage("코칭 메시지를 찾을 수 없습니다.");

        verify(pronunciationResultRepository, never()).save(any(CoachingPronunciationResult.class));
    }

    @Test
    @DisplayName("발음 평가 결과 저장 실패 - 스크립트 턴이 존재하지 않는다")
    void 발음_평가_결과_저장_실패_스크립트턴없음() {
        CoachingPronunciationResultDto.RequestSavePronunciationResult request =
                CoachingPronunciationResultDto.RequestSavePronunciationResult.builder()
                        .coachingMessageId(10L)
                        .coachingScriptTurnId(999L)
                        .recognizedText("hello")
                        .build();

        when(coachingMessageRepository.findById(10L)).thenReturn(Optional.of(userMessage));
        when(coachingScriptTurnRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pronunciationResultService.savePronunciationResult(request))
                .isInstanceOf(CoachingScriptTurnNotFoundException.class)
                .hasMessage("코칭 스크립트 턴을 찾을 수 없습니다.");

        verify(pronunciationResultRepository, never()).save(any(CoachingPronunciationResult.class));
    }

    @Test
    @DisplayName("메시지 ID 기준 발음 평가 결과 조회 성공")
    void 메시지_ID_기준_발음_평가_결과_조회_성공() {
        when(pronunciationResultRepository.findByCoachingMessage_CoachingMessageId(10L))
                .thenReturn(Optional.of(pronunciationResult));

        CoachingPronunciationResultDto.ResponsePronunciationResult response =
                pronunciationResultService.getPronunciationResultByMessageId(10L);

        assertThat(response.getPronunciationResultId()).isEqualTo(1000L);
        assertThat(response.getCoachingMessageId()).isEqualTo(10L);
        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("메시지 ID 기준 발음 평가 결과 조회 실패 - 결과가 존재하지 않는다")
    void 메시지_ID_기준_발음_평가_결과_조회_실패_결과없음() {
        when(pronunciationResultRepository.findByCoachingMessage_CoachingMessageId(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> pronunciationResultService.getPronunciationResultByMessageId(999L))
                .isInstanceOf(CoachingPronunciationResultNotFoundException.class)
                .hasMessage("발음 평가 결과를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("코칭 세션 ID 기준 발음 평가 결과 목록 조회 성공")
    void 코칭_세션_ID_기준_발음_평가_결과_목록_조회_성공() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(pronunciationResultRepository
                .findByCoachingScriptTurn_CoachingSession_CoachingSessionIdOrderByCreatedAtAsc(100L))
                .thenReturn(List.of(pronunciationResult));

        CoachingPronunciationResultDto.ResponseGetPronunciationResults response =
                pronunciationResultService.getPronunciationResults(100L);

        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getPronunciationResults()).hasSize(1);
        assertThat(response.getPronunciationResults().get(0).getPronunciationScore()).isEqualTo(87.8);
    }

    @Test
    @DisplayName("코칭 세션 ID 기준 발음 평가 결과 목록 조회 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_세션_ID_기준_발음_평가_결과_목록_조회_실패_세션없음() {
        when(coachingSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pronunciationResultService.getPronunciationResults(999L))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(pronunciationResultRepository, never())
                .findByCoachingScriptTurn_CoachingSession_CoachingSessionIdOrderByCreatedAtAsc(999L);
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