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

import kr.co.mapspring.ai.dto.CoachingScriptTurnDto;
import kr.co.mapspring.ai.entity.CoachingScriptTurn;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingScriptTurnRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.impl.CoachingScriptTurnServiceImpl;
import kr.co.mapspring.global.exception.ai.CoachingScriptTurnNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;

@ExtendWith(MockitoExtension.class)
class CoachingScriptTurnServiceTest {

    @InjectMocks
    private CoachingScriptTurnServiceImpl coachingScriptTurnService;

    @Mock
    private CoachingScriptTurnRepository coachingScriptTurnRepository;

    @Mock
    private CoachingSessionRepository coachingSessionRepository;

    private CoachingSession coachingSession;
    private CoachingScriptTurn scriptTurn1;
    private CoachingScriptTurn scriptTurn2;

    @BeforeEach
    void setUp() throws Exception {
        LearningSession learningSession = createInstance(LearningSession.class);
        setField(learningSession, "sessionId", 10L);

        coachingSession = createInstance(CoachingSession.class);
        setField(coachingSession, "coachingSessionId", 100L);
        setField(coachingSession, "learningSession", learningSession);
        setField(coachingSession, "coachingSessionStatus", CoachingSessionStatus.RUNNING);
        setField(coachingSession, "selectedOption", "WORD");
        setField(coachingSession, "currentTurnOrder", 0);

        scriptTurn1 = createInstance(CoachingScriptTurn.class);
        setField(scriptTurn1, "coachingScriptTurnId", 1L);
        setField(scriptTurn1, "coachingSession", coachingSession);
        setField(scriptTurn1, "turnOrder", 1);
        setField(scriptTurn1, "assistantText", "Good morning. What would you like to order?");
        setField(scriptTurn1, "expectedText", "I would like a latte with almond milk, please.");
        setField(scriptTurn1, "createdAt", LocalDateTime.of(2026, 5, 3, 10, 0));

        scriptTurn2 = createInstance(CoachingScriptTurn.class);
        setField(scriptTurn2, "coachingScriptTurnId", 2L);
        setField(scriptTurn2, "coachingSession", coachingSession);
        setField(scriptTurn2, "turnOrder", 2);
        setField(scriptTurn2, "assistantText", "Sure. Would you like it hot or iced?");
        setField(scriptTurn2, "expectedText", "Hot, please.");
        setField(scriptTurn2, "createdAt", LocalDateTime.of(2026, 5, 3, 10, 1));
    }

    @Test
    @DisplayName("코칭 스크립트 턴 저장 성공")
    void 코칭_스크립트_턴_저장_성공() throws Exception {
        CoachingScriptTurnDto.RequestSaveCoachingScriptTurn request =
                CoachingScriptTurnDto.RequestSaveCoachingScriptTurn.builder()
                        .coachingSessionId(100L)
                        .turnOrder(1)
                        .assistantText("Good morning. What would you like to order?")
                        .expectedText("I would like a latte with almond milk, please.")
                        .build();

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.save(any(CoachingScriptTurn.class))).thenReturn(scriptTurn1);

        CoachingScriptTurnDto.ResponseCoachingScriptTurn response =
                coachingScriptTurnService.saveScriptTurn(request);

        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getTurnOrder()).isEqualTo(1);
        assertThat(response.getAssistantText()).isEqualTo("Good morning. What would you like to order?");
        assertThat(response.getExpectedText()).isEqualTo("I would like a latte with almond milk, please.");

        verify(coachingScriptTurnRepository).save(any(CoachingScriptTurn.class));
    }

    @Test
    @DisplayName("코칭 스크립트 턴 저장 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_스크립트_턴_저장_실패_세션없음() {
        CoachingScriptTurnDto.RequestSaveCoachingScriptTurn request =
                CoachingScriptTurnDto.RequestSaveCoachingScriptTurn.builder()
                        .coachingSessionId(999L)
                        .turnOrder(1)
                        .assistantText("Good morning.")
                        .expectedText("I would like a latte.")
                        .build();

        when(coachingSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingScriptTurnService.saveScriptTurn(request))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(coachingScriptTurnRepository, never()).save(any(CoachingScriptTurn.class));
    }

    @Test
    @DisplayName("코칭 스크립트 턴 목록 저장 성공")
    void 코칭_스크립트_턴_목록_저장_성공() {
        List<CoachingScriptTurnDto.RequestSaveCoachingScriptTurn> requests = List.of(
                CoachingScriptTurnDto.RequestSaveCoachingScriptTurn.builder()
                        .coachingSessionId(100L)
                        .turnOrder(1)
                        .assistantText("Good morning. What would you like to order?")
                        .expectedText("I would like a latte with almond milk, please.")
                        .build(),
                CoachingScriptTurnDto.RequestSaveCoachingScriptTurn.builder()
                        .coachingSessionId(100L)
                        .turnOrder(2)
                        .assistantText("Sure. Would you like it hot or iced?")
                        .expectedText("Hot, please.")
                        .build()
        );

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.saveAll(any())).thenReturn(List.of(scriptTurn1, scriptTurn2));

        List<CoachingScriptTurnDto.ResponseCoachingScriptTurn> responses =
                coachingScriptTurnService.saveScriptTurns(100L, requests);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getTurnOrder()).isEqualTo(1);
        assertThat(responses.get(1).getTurnOrder()).isEqualTo(2);

        verify(coachingScriptTurnRepository).saveAll(any());
    }

    @Test
    @DisplayName("코칭 스크립트 턴 단건 조회 성공")
    void 코칭_스크립트_턴_단건_조회_성공() {
        when(coachingScriptTurnRepository
                .findByCoachingSession_CoachingSessionIdAndTurnOrder(100L, 1))
                .thenReturn(Optional.of(scriptTurn1));

        CoachingScriptTurnDto.ResponseCoachingScriptTurn response =
                coachingScriptTurnService.getScriptTurn(100L, 1);

        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getTurnOrder()).isEqualTo(1);
        assertThat(response.getAssistantText()).isEqualTo("Good morning. What would you like to order?");
    }

    @Test
    @DisplayName("코칭 스크립트 턴 단건 조회 실패 - 스크립트 턴이 존재하지 않는다")
    void 코칭_스크립트_턴_단건_조회_실패_스크립트턴없음() {
        when(coachingScriptTurnRepository
                .findByCoachingSession_CoachingSessionIdAndTurnOrder(100L, 999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingScriptTurnService.getScriptTurn(100L, 999))
                .isInstanceOf(CoachingScriptTurnNotFoundException.class)
                .hasMessage("코칭 스크립트 턴을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("코칭 스크립트 턴 목록 조회 성공")
    void 코칭_스크립트_턴_목록_조회_성공() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository
                .findByCoachingSession_CoachingSessionIdOrderByTurnOrderAsc(100L))
                .thenReturn(List.of(scriptTurn1, scriptTurn2));

        CoachingScriptTurnDto.ResponseGetCoachingScriptTurns response =
                coachingScriptTurnService.getScriptTurns(100L);

        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getTurns()).hasSize(2);
        assertThat(response.getTurns().get(0).getTurnOrder()).isEqualTo(1);
        assertThat(response.getTurns().get(1).getTurnOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("코칭 스크립트 턴 목록 조회 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_스크립트_턴_목록_조회_실패_세션없음() {
        when(coachingSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingScriptTurnService.getScriptTurns(999L))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(coachingScriptTurnRepository, never())
                .findByCoachingSession_CoachingSessionIdOrderByTurnOrderAsc(999L);
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