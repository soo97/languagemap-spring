package kr.co.mapspring.ai.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.dto.StartCoachingWithInitialMessageDto;
import kr.co.mapspring.ai.service.CoachingFlowService;
import kr.co.mapspring.ai.service.CoachingMessageService;
import kr.co.mapspring.ai.service.StartCoachingSessionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CoachingFlowServiceImpl implements CoachingFlowService {

    private final StartCoachingSessionService startCoachingSessionService;
    private final CoachingMessageService coachingMessageService;

    @Override
    public StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage startCoachingWithInitialMessage(
            StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request
    ) {
        StartCoachingSessionDto.RequestStartCoachingSession startRequest =
                StartCoachingSessionDto.RequestStartCoachingSession.builder()
                        .sessionId(request.getSessionId())
                        .optionType(request.getOptionType())
                        .build();

        StartCoachingSessionDto.ResponseStartCoachingSession sessionResponse =
                startCoachingSessionService.startCoachingSession(startRequest);

        String initialMessage = createInitialMessage(request.getOptionType());

        CoachingMessageDto.ResponseCoachingMessage savedInitialMessage =
                coachingMessageService.saveAssistantMessage(
                        sessionResponse.getCoachingSessionId(),
                        null,
                        initialMessage,
                        null
                );

        return StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage.builder()
                .coachingSessionId(sessionResponse.getCoachingSessionId())
                .sessionId(sessionResponse.getSessionId())
                .coachingSessionStatus(sessionResponse.getCoachingSessionStatus())
                .selectedOption(sessionResponse.getSelectedOption())
                .currentTurnOrder(sessionResponse.getCurrentTurnOrder())
                .initialMessage(savedInitialMessage)
                .build();
    }

    private String createInitialMessage(String optionType) {
        if ("WORD".equals(optionType)) {
            return "좋아요. 이번에는 더 어려운 단어를 사용해서 자연스럽게 말하는 연습을 해볼게요.";
        }

        if ("GRAMMAR".equals(optionType)) {
            return "좋아요. 이번에는 더 자연스럽고 정확한 문법을 사용해서 말하는 연습을 해볼게요.";
        }

        if ("DIALOGUE".equals(optionType)) {
            return "좋아요. 이번에는 더 많은 대화를 이어가면서 자연스럽게 말하는 연습을 해볼게요.";
        }

        return "좋아요. 지금부터 AI 코칭을 시작해볼게요.";
    }
}