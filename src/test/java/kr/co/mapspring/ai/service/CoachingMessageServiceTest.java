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

import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.entity.CoachingScriptTurn;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingMessageRepository;
import kr.co.mapspring.ai.repository.CoachingScriptTurnRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.impl.CoachingMessageServiceImpl;
import kr.co.mapspring.global.exception.ai.CoachingScriptTurnNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import kr.co.mapspring.global.exception.ai.InvalidCoachingMessageException;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.global.exception.ai.CoachingMessageRoleRequiredException;
import kr.co.mapspring.global.exception.ai.AssistantMessageRequiredException;

@ExtendWith(MockitoExtension.class)
class CoachingMessageServiceTest {

    @InjectMocks
    private CoachingMessageServiceImpl coachingMessageService;

    @Mock
    private CoachingMessageRepository coachingMessageRepository;

    @Mock
    private CoachingSessionRepository coachingSessionRepository;

    @Mock
    private CoachingScriptTurnRepository coachingScriptTurnRepository;

    private CoachingSession coachingSession;
    private CoachingScriptTurn coachingScriptTurn;
    private CoachingMessage userMessage;
    private CoachingMessage assistantMessage;

    @BeforeEach
    void setUp() throws Exception {
        LearningSession learningSession = createInstance(LearningSession.class);
        setField(learningSession, "sessionId", 10L);

        coachingSession = createInstance(CoachingSession.class);
        setField(coachingSession, "coachingSessionId", 100L);
        setField(coachingSession, "learningSession", learningSession);
        setField(coachingSession, "coachingSessionStatus", CoachingSessionStatus.RUNNING);

        coachingScriptTurn = createInstance(CoachingScriptTurn.class);
        setField(coachingScriptTurn, "coachingScriptTurnId", 1L);
        setField(coachingScriptTurn, "coachingSession", coachingSession);
        setField(coachingScriptTurn, "turnOrder", 1);
        setField(coachingScriptTurn, "assistantText", "Good morning. What would you like to order?");
        setField(coachingScriptTurn, "expectedText", "I would like a latte, please.");
        setField(coachingScriptTurn, "createdAt", LocalDateTime.of(2026, 4, 25, 9, 59));

        userMessage = createInstance(CoachingMessage.class);
        setField(userMessage, "coachingMessageId", 1L);
        setField(userMessage, "coachingSession", coachingSession);
        setField(userMessage, "coachingScriptTurn", coachingScriptTurn);
        setField(userMessage, "role", CoachingMessageRole.USER);
        setField(userMessage, "message", "Could you make it less sweet?");
        setField(userMessage, "audioUrl", "/static/uploads/user-1.wav");
        setField(userMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 0));

        assistantMessage = createInstance(CoachingMessage.class);
        setField(assistantMessage, "coachingMessageId", 2L);
        setField(assistantMessage, "coachingSession", coachingSession);
        setField(assistantMessage, "coachingScriptTurn", coachingScriptTurn);
        setField(assistantMessage, "role", CoachingMessageRole.ASSISTANT);
        setField(assistantMessage, "message", "Sure. Would you like anything else?");
        setField(assistantMessage, "audioUrl", "/static/audio/assistant-1.mp3");
        setField(assistantMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 1));
    }

    @Test
    @DisplayName("코칭 메시지 저장 성공 - USER 메시지 저장")
    void USER_메시지_저장_성공() throws Exception {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(1L)
                        .role(CoachingMessageRole.USER)
                        .message("hello")
                        .audioUrl("/static/uploads/user-hello.wav")
                        .build();

        CoachingMessage savedMessage = createInstance(CoachingMessage.class);
        setField(savedMessage, "coachingMessageId", 1L);
        setField(savedMessage, "coachingSession", coachingSession);
        setField(savedMessage, "coachingScriptTurn", coachingScriptTurn);
        setField(savedMessage, "role", CoachingMessageRole.USER);
        setField(savedMessage, "message", "hello");
        setField(savedMessage, "audioUrl", "/static/uploads/user-hello.wav");
        setField(savedMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 0));

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.findById(1L)).thenReturn(Optional.of(coachingScriptTurn));
        when(coachingMessageRepository.save(any(CoachingMessage.class))).thenReturn(savedMessage);

        CoachingMessageDto.ResponseCoachingMessage response =
                coachingMessageService.saveCoachingMessage(request);

        assertThat(response.getCoachingMessageId()).isEqualTo(1L);
        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getRole()).isEqualTo(CoachingMessageRole.USER);
        assertThat(response.getMessage()).isEqualTo("hello");
        assertThat(response.getAudioUrl()).isEqualTo("/static/uploads/user-hello.wav");

        verify(coachingMessageRepository).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 성공 - ASSISTANT 메시지 저장")
    void ASSISTANT_메시지_저장_성공() throws Exception {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(1L)
                        .role(CoachingMessageRole.ASSISTANT)
                        .message("Hi")
                        .audioUrl("/static/audio/assistant-hi.mp3")
                        .build();

        CoachingMessage savedMessage = createInstance(CoachingMessage.class);
        setField(savedMessage, "coachingMessageId", 2L);
        setField(savedMessage, "coachingSession", coachingSession);
        setField(savedMessage, "coachingScriptTurn", coachingScriptTurn);
        setField(savedMessage, "role", CoachingMessageRole.ASSISTANT);
        setField(savedMessage, "message", "Hi");
        setField(savedMessage, "audioUrl", "/static/audio/assistant-hi.mp3");
        setField(savedMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 1));

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.findById(1L)).thenReturn(Optional.of(coachingScriptTurn));
        when(coachingMessageRepository.save(any(CoachingMessage.class))).thenReturn(savedMessage);

        CoachingMessageDto.ResponseCoachingMessage response =
                coachingMessageService.saveCoachingMessage(request);

        assertThat(response.getCoachingMessageId()).isEqualTo(2L);
        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getRole()).isEqualTo(CoachingMessageRole.ASSISTANT);
        assertThat(response.getMessage()).isEqualTo("Hi");
        assertThat(response.getAudioUrl()).isEqualTo("/static/audio/assistant-hi.mp3");

        verify(coachingMessageRepository).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 성공 - 스크립트 턴이 없어도 저장")
    void 스크립트턴_null_메시지_저장_성공() throws Exception {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(null)
                        .role(CoachingMessageRole.USER)
                        .message("옵션 선택 메시지")
                        .audioUrl(null)
                        .build();

        CoachingMessage savedMessage = createInstance(CoachingMessage.class);
        setField(savedMessage, "coachingMessageId", 3L);
        setField(savedMessage, "coachingSession", coachingSession);
        setField(savedMessage, "coachingScriptTurn", null);
        setField(savedMessage, "role", CoachingMessageRole.USER);
        setField(savedMessage, "message", "옵션 선택 메시지");
        setField(savedMessage, "audioUrl", null);
        setField(savedMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 2));

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingMessageRepository.save(any(CoachingMessage.class))).thenReturn(savedMessage);

        CoachingMessageDto.ResponseCoachingMessage response =
                coachingMessageService.saveCoachingMessage(request);

        assertThat(response.getCoachingMessageId()).isEqualTo(3L);
        assertThat(response.getCoachingScriptTurnId()).isNull();
        assertThat(response.getRole()).isEqualTo(CoachingMessageRole.USER);
        assertThat(response.getMessage()).isEqualTo("옵션 선택 메시지");

        verify(coachingScriptTurnRepository, never()).findById(any());
        verify(coachingMessageRepository).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 실패 - 스크립트 턴이 존재하지 않는다")
    void 코칭_메시지_저장_실패_스크립트턴없음() {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(999L)
                        .role(CoachingMessageRole.USER)
                        .message("hello")
                        .audioUrl("/static/uploads/user-hello.wav")
                        .build();

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingMessageService.saveCoachingMessage(request))
                .isInstanceOf(CoachingScriptTurnNotFoundException.class)
                .hasMessage("코칭 스크립트 턴을 찾을 수 없습니다.");

        verify(coachingMessageRepository, never()).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 실패 - ASSISTANT 메시지가 null이다")
    void ASSISTANT_null_메시지_저장_실패() {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(1L)
                        .role(CoachingMessageRole.ASSISTANT)
                        .message(null)
                        .audioUrl("/static/audio/assistant-null.mp3")
                        .build();

        assertThatThrownBy(() -> coachingMessageService.saveCoachingMessage(request))
                .isInstanceOf(AssistantMessageRequiredException.class)
                .hasMessage("AI 메시지는 비어 있을 수 없습니다.");

        verify(coachingMessageRepository, never()).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 실패 - ASSISTANT 메시지가 빈 문자열이다")
    void ASSISTANT_빈문자열_메시지_저장_실패() {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(1L)
                        .role(CoachingMessageRole.ASSISTANT)
                        .message("")
                        .audioUrl("/static/audio/assistant-empty.mp3")
                        .build();

        assertThatThrownBy(() -> coachingMessageService.saveCoachingMessage(request))
        		.isInstanceOf(AssistantMessageRequiredException.class)
                .hasMessage("AI 메시지는 비어 있을 수 없습니다.");

        verify(coachingMessageRepository, never()).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_메시지_저장_실패_세션없음() {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(999L)
                        .coachingScriptTurnId(1L)
                        .role(CoachingMessageRole.USER)
                        .message("hello")
                        .audioUrl("/static/uploads/user-hello.wav")
                        .build();

        when(coachingSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingMessageService.saveCoachingMessage(request))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(coachingScriptTurnRepository, never()).findById(any());
        verify(coachingMessageRepository, never()).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 저장 실패 - 요청 데이터에 role이 없다")
    void 코칭_메시지_저장_실패_role없음() {
        CoachingMessageDto.RequestSaveCoachingMessage request =
                CoachingMessageDto.RequestSaveCoachingMessage.builder()
                        .coachingSessionId(100L)
                        .coachingScriptTurnId(1L)
                        .role(null)
                        .message("hello")
                        .audioUrl("/static/uploads/user-hello.wav")
                        .build();

        assertThatThrownBy(() -> coachingMessageService.saveCoachingMessage(request))
                .isInstanceOf(CoachingMessageRoleRequiredException.class)
                .hasMessage("메시지 역할은 필수입니다.");

        verify(coachingMessageRepository, never()).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("USER 메시지 저장 성공 - 내부 흐름용")
    void USER_메시지_내부저장_성공() throws Exception {
        CoachingMessage savedMessage = createInstance(CoachingMessage.class);
        setField(savedMessage, "coachingMessageId", 5L);
        setField(savedMessage, "coachingSession", coachingSession);
        setField(savedMessage, "coachingScriptTurn", coachingScriptTurn);
        setField(savedMessage, "role", CoachingMessageRole.USER);
        setField(savedMessage, "message", "I would like a latte.");
        setField(savedMessage, "audioUrl", "/static/uploads/user-turn-1.wav");
        setField(savedMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 4));

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.findById(1L)).thenReturn(Optional.of(coachingScriptTurn));
        when(coachingMessageRepository.save(any(CoachingMessage.class))).thenReturn(savedMessage);

        CoachingMessageDto.ResponseCoachingMessage response =
                coachingMessageService.saveUserMessage(
                        100L,
                        1L,
                        "I would like a latte.",
                        "/static/uploads/user-turn-1.wav"
                );

        assertThat(response.getCoachingMessageId()).isEqualTo(5L);
        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getRole()).isEqualTo(CoachingMessageRole.USER);
        assertThat(response.getMessage()).isEqualTo("I would like a latte.");
        assertThat(response.getAudioUrl()).isEqualTo("/static/uploads/user-turn-1.wav");

        verify(coachingMessageRepository).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("ASSISTANT 메시지 저장 성공 - 내부 흐름용")
    void ASSISTANT_메시지_내부저장_성공() throws Exception {
        CoachingMessage savedMessage = createInstance(CoachingMessage.class);
        setField(savedMessage, "coachingMessageId", 6L);
        setField(savedMessage, "coachingSession", coachingSession);
        setField(savedMessage, "coachingScriptTurn", coachingScriptTurn);
        setField(savedMessage, "role", CoachingMessageRole.ASSISTANT);
        setField(savedMessage, "message", "Good morning. What would you like to order?");
        setField(savedMessage, "audioUrl", "/static/audio/assistant-turn-1.mp3");
        setField(savedMessage, "createdAt", LocalDateTime.of(2026, 4, 25, 10, 5));

        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingScriptTurnRepository.findById(1L)).thenReturn(Optional.of(coachingScriptTurn));
        when(coachingMessageRepository.save(any(CoachingMessage.class))).thenReturn(savedMessage);

        CoachingMessageDto.ResponseCoachingMessage response =
                coachingMessageService.saveAssistantMessage(
                        100L,
                        1L,
                        "Good morning. What would you like to order?",
                        "/static/audio/assistant-turn-1.mp3"
                );

        assertThat(response.getCoachingMessageId()).isEqualTo(6L);
        assertThat(response.getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getRole()).isEqualTo(CoachingMessageRole.ASSISTANT);
        assertThat(response.getMessage()).isEqualTo("Good morning. What would you like to order?");
        assertThat(response.getAudioUrl()).isEqualTo("/static/audio/assistant-turn-1.mp3");

        verify(coachingMessageRepository).save(any(CoachingMessage.class));
    }

    @Test
    @DisplayName("코칭 메시지 목록 조회 실패 - 코칭 세션이 존재하지 않는다")
    void 코칭_메시지_목록_조회_실패_세션없음() {
        Long coachingSessionId = 999L;

        when(coachingSessionRepository.findById(coachingSessionId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachingMessageService.getCoachingMessages(coachingSessionId))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(coachingMessageRepository, never())
                .findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(coachingSessionId);
    }

    @Test
    @DisplayName("코칭 메시지 목록 조회 성공")
    void 코칭_메시지_목록_조회_성공() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingMessageRepository.findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(100L))
                .thenReturn(List.of(userMessage, assistantMessage));

        CoachingMessageDto.ResponseGetCoachingMessages response =
                coachingMessageService.getCoachingMessages(100L);

        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getMessages()).hasSize(2);
        assertThat(response.getMessages().get(0).getRole()).isEqualTo(CoachingMessageRole.USER);
        assertThat(response.getMessages().get(0).getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getMessages().get(0).getAudioUrl()).isEqualTo("/static/uploads/user-1.wav");
        assertThat(response.getMessages().get(1).getRole()).isEqualTo(CoachingMessageRole.ASSISTANT);
        assertThat(response.getMessages().get(1).getCoachingScriptTurnId()).isEqualTo(1L);
        assertThat(response.getMessages().get(1).getAudioUrl()).isEqualTo("/static/audio/assistant-1.mp3");
    }

    @Test
    @DisplayName("코칭 메시지 목록 조회 성공 - 메시지가 없으면 빈 리스트 반환")
    void 코칭_메시지_목록_조회_성공_빈리스트() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingMessageRepository.findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(100L))
                .thenReturn(List.of());

        CoachingMessageDto.ResponseGetCoachingMessages response =
                coachingMessageService.getCoachingMessages(100L);

        assertThat(response.getCoachingSessionId()).isEqualTo(100L);
        assertThat(response.getMessages()).isEmpty();
    }

    @Test
    @DisplayName("코칭 메시지 목록 조회 성공 - 생성시간 오름차순")
    void 코칭_메시지_목록_조회_성공_오름차순() {
        when(coachingSessionRepository.findById(100L)).thenReturn(Optional.of(coachingSession));
        when(coachingMessageRepository.findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(100L))
                .thenReturn(List.of(userMessage, assistantMessage));

        CoachingMessageDto.ResponseGetCoachingMessages response =
                coachingMessageService.getCoachingMessages(100L);

        assertThat(response.getMessages().get(0).getCreatedAt())
                .isBefore(response.getMessages().get(1).getCreatedAt());
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