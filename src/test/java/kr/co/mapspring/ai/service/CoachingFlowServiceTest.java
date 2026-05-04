package kr.co.mapspring.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.dto.StartCoachingWithInitialMessageDto;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import kr.co.mapspring.ai.service.impl.CoachingFlowServiceImpl;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;

@ExtendWith(MockitoExtension.class)
class CoachingFlowServiceTest {

    @Mock
    private StartCoachingSessionService startCoachingSessionService;

    @Mock
    private CoachingMessageService coachingMessageService;

    @InjectMocks
    private CoachingFlowServiceImpl coachingFlowService;

    @Test
    @DisplayName("AI 코칭 시작 시 optionType 기준 초기 ASSISTANT 메시지를 저장하고 반환한다")
    void startCoachingWithInitialMessage_success() {

        // given
        StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request =
                StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage.builder()
                        .sessionId(1L)
                        .optionType("WORD")
                        .build();

        StartCoachingSessionDto.ResponseStartCoachingSession sessionResponse =
                StartCoachingSessionDto.ResponseStartCoachingSession.builder()
                        .coachingSessionId(10L)
                        .sessionId(1L)
                        .coachingSessionStatus("RUNNING")
                        .selectedOption("WORD")
                        .currentTurnOrder(0)
                        .build();

        CoachingMessageDto.ResponseCoachingMessage initialMessage =
                CoachingMessageDto.ResponseCoachingMessage.builder()
                        .coachingMessageId(100L)
                        .coachingSessionId(10L)
                        .coachingScriptTurnId(null)
                        .role(CoachingMessageRole.ASSISTANT) // ✅ enum
                        .message("좋아요. 이번에는 더 어려운 단어를 사용해서 자연스럽게 말하는 연습을 해볼게요.")
                        .audioUrl(null)
                        .build();

        when(startCoachingSessionService.startCoachingSession(any()))
                .thenReturn(sessionResponse);

        when(coachingMessageService.saveAssistantMessage(
                10L,
                null,
                "좋아요. 이번에는 더 어려운 단어를 사용해서 자연스럽게 말하는 연습을 해볼게요.",
                null
        )).thenReturn(initialMessage);

        // when
        StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage response =
                coachingFlowService.startCoachingWithInitialMessage(request);

        // then
        assertThat(response.getCoachingSessionId()).isEqualTo(10L);
        assertThat(response.getSessionId()).isEqualTo(1L);
        assertThat(response.getCoachingSessionStatus()).isEqualTo("RUNNING");
        assertThat(response.getSelectedOption()).isEqualTo("WORD");
        assertThat(response.getCurrentTurnOrder()).isEqualTo(0);
        assertThat(response.getInitialMessage()).isNotNull();
        assertThat(response.getInitialMessage().getRole())
                .isEqualTo(CoachingMessageRole.ASSISTANT);
        assertThat(response.getInitialMessage().getMessage())
                .contains("더 어려운 단어");

        verify(startCoachingSessionService).startCoachingSession(any());

        verify(coachingMessageService).saveAssistantMessage(
                10L,
                null,
                "좋아요. 이번에는 더 어려운 단어를 사용해서 자연스럽게 말하는 연습을 해볼게요.",
                null
        );   
    }
    
    @Test
    @DisplayName("AI 코칭 시작 실패 - StartCoachingSessionService에서 예외 발생 시 그대로 전달한다")
    void startCoachingWithInitialMessage_startSessionFail() {
        // given
        StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request =
                StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage.builder()
                        .sessionId(999L)
                        .optionType("WORD")
                        .build();

        when(startCoachingSessionService.startCoachingSession(any()))
                .thenThrow(new LearningSessionNotFoundException());

        // when & then
        assertThatThrownBy(() -> coachingFlowService.startCoachingWithInitialMessage(request))
                .isInstanceOf(LearningSessionNotFoundException.class);
    }
    
    @Test
    @DisplayName("AI 코칭 시작 실패 - 초기 ASSISTANT 메시지 저장 중 예외 발생 시 그대로 전달한다")
    void startCoachingWithInitialMessage_saveInitialMessageFail() {
        // given
        StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request =
                StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage.builder()
                        .sessionId(1L)
                        .optionType("WORD")
                        .build();

        StartCoachingSessionDto.ResponseStartCoachingSession sessionResponse =
                StartCoachingSessionDto.ResponseStartCoachingSession.builder()
                        .coachingSessionId(10L)
                        .sessionId(1L)
                        .coachingSessionStatus("RUNNING")
                        .selectedOption("WORD")
                        .currentTurnOrder(0)
                        .build();

        when(startCoachingSessionService.startCoachingSession(any()))
                .thenReturn(sessionResponse);

        when(coachingMessageService.saveAssistantMessage(any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        // when & then
        assertThatThrownBy(() -> coachingFlowService.startCoachingWithInitialMessage(request))
                .isInstanceOf(RuntimeException.class);
    }
}