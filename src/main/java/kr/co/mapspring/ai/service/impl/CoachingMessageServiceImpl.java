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
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import kr.co.mapspring.global.exception.ai.InvalidCoachingMessageException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingMessageServiceImpl implements CoachingMessageService {

    private final CoachingMessageRepository coachingMessageRepository;
    private final CoachingSessionRepository coachingSessionRepository;

    @Override
    @Transactional
    public CoachingMessageDto.ResponseCoachingMessage saveCoachingMessage(
            CoachingMessageDto.RequestSaveCoachingMessage request
    ) {
        if (request.getRole() == null) {
            throw new InvalidCoachingMessageException("메시지 역할은 필수입니다.");
        }

        if (request.getRole() == CoachingMessageRole.ASSISTANT
                && (request.getMessage() == null || request.getMessage().isBlank())) {
            throw new InvalidCoachingMessageException("AI 메시지는 비어 있을 수 없습니다.");
        }

        CoachingSession coachingSession = coachingSessionRepository.findById(request.getCoachingSessionId())
                .orElseThrow(CoachingSessionNotFoundException::new);

        CoachingMessage coachingMessage = CoachingMessage.create(
                coachingSession,
                request.getRole(),
                request.getMessage()
        );

        CoachingMessage savedCoachingMessage = coachingMessageRepository.save(coachingMessage);

        return CoachingMessageDto.ResponseCoachingMessage.from(savedCoachingMessage);
    }

    @Override
    public CoachingMessageDto.ResponseGetCoachingMessages getCoachingMessages(Long coachingSessionId) {
        coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        List<CoachingMessageDto.ResponseCoachingMessage> messages = coachingMessageRepository
                .findByCoachingSession_CoachingSessionIdOrderByCreatedAtAsc(coachingSessionId)
                .stream()
                .map(CoachingMessageDto.ResponseCoachingMessage::from)
                .toList();

        return CoachingMessageDto.ResponseGetCoachingMessages.builder()
                .coachingSessionId(coachingSessionId)
                .messages(messages)
                .build();
    }
}