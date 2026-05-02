package kr.co.mapspring.ai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import kr.co.mapspring.ai.repository.CoachingMessageRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.CoachingMessageService;
import kr.co.mapspring.global.exception.ai.AssistantMessageRequiredException;
import kr.co.mapspring.global.exception.ai.CoachingMessageRoleRequiredException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingMessageServiceImpl implements CoachingMessageService {

    private final CoachingMessageRepository coachingMessageRepository;
    private final CoachingSessionRepository coachingSessionRepository;

    // DTO 기반 메시지 저장 (공통 저장용)
    @Override
    @Transactional
    public CoachingMessageDto.ResponseCoachingMessage saveCoachingMessage(
            CoachingMessageDto.RequestSaveCoachingMessage request
    ) {
        if (request.getRole() == null) {
            throw new CoachingMessageRoleRequiredException();
        }

        if (request.getRole() == CoachingMessageRole.ASSISTANT
                && (request.getMessage() == null || request.getMessage().isBlank())) {
            throw new AssistantMessageRequiredException();
        }

        CoachingSession coachingSession = coachingSessionRepository.findById(request.getCoachingSessionId())
                .orElseThrow(CoachingSessionNotFoundException::new);

        CoachingMessage coachingMessage = CoachingMessage.create(
                coachingSession,
                request.getRole(),
                request.getMessage(),
                request.getAudioUrl()
        );

        CoachingMessage saved = coachingMessageRepository.save(coachingMessage);

        return CoachingMessageDto.ResponseCoachingMessage.from(saved);
    }

    // 메시지 목록 조회
    @Override
    public CoachingMessageDto.ResponseGetCoachingMessages getCoachingMessages(Long coachingSessionId) {

        coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        List<CoachingMessageDto.ResponseCoachingMessage> messages =
                coachingMessageRepository
                        .findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(coachingSessionId)
                        .stream()
                        .map(CoachingMessageDto.ResponseCoachingMessage::from)
                        .toList();

        return CoachingMessageDto.ResponseGetCoachingMessages.builder()
                .coachingSessionId(coachingSessionId)
                .messages(messages)
                .build();
    }

    // USER 메시지 저장 (STT 결과)
    @Override
    @Transactional
    public CoachingMessageDto.ResponseCoachingMessage saveUserMessage(
            Long coachingSessionId,
            String message,
            String audioUrl
    ) {
        CoachingSession session = coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        CoachingMessage coachingMessage = CoachingMessage.create(
                session,
                CoachingMessageRole.USER,
                message,
                audioUrl
        );

        CoachingMessage saved = coachingMessageRepository.save(coachingMessage);

        return CoachingMessageDto.ResponseCoachingMessage.from(saved);
    }

    // ASSISTANT 메시지 저장 (TTS 포함)
    @Override
    @Transactional
    public CoachingMessageDto.ResponseCoachingMessage saveAssistantMessage(
            Long coachingSessionId,
            String message,
            String audioUrl
    ) {
        if (message == null || message.isBlank()) {
            throw new AssistantMessageRequiredException();
        }

        CoachingSession session = coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        CoachingMessage coachingMessage = CoachingMessage.create(
                session,
                CoachingMessageRole.ASSISTANT,
                message,
                audioUrl
        );

        CoachingMessage saved = coachingMessageRepository.save(coachingMessage);

        return CoachingMessageDto.ResponseCoachingMessage.from(saved);
    }
}