package kr.co.mapspring.ai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingEntryDto;
import kr.co.mapspring.ai.service.CoachingEntryService;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.SessionEvaluation;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.SessionEvaluationRepository;
import kr.co.mapspring.place.repository.SessionMessageRepository;
import lombok.RequiredArgsConstructor;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingEntryServiceImpl implements CoachingEntryService {

    private final LearningSessionRepository learningSessionRepository;
    private final SessionMessageRepository sessionMessageRepository;
    private final SessionEvaluationRepository sessionEvaluationRepository;

    @Override
    public CoachingEntryDto.ResponseGetCoachingEntry getCoachingEntryData(Long sessionId) {
        LearningSession learningSession = learningSessionRepository.findBySessionId(sessionId)
        		.orElseThrow(LearningSessionNotFoundException::new);

        Place place = learningSession.getPlace();
        Region region = place.getRegion();

        List<SessionMessage> sessionMessages =
                sessionMessageRepository.findBySession_SessionIdOrderByCreatedAtAsc(sessionId);

        String evaluation = sessionEvaluationRepository.findBySession_SessionId(sessionId)
                .map(SessionEvaluation::getEvaluation)
                .orElse("");

        List<CoachingEntryDto.ResponseMessageItem> messageItems = sessionMessages.stream()
                .map(this::toMessageItem)
                .toList();

        return CoachingEntryDto.ResponseGetCoachingEntry.builder()
                .sessionId(learningSession.getSessionId())
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .country(region.getCountry())
                .city(region.getCity())
                .placeDescription(place.getPlaceDescription())
                .evaluation(evaluation)
                .sessionMessages(messageItems)
                .build();
    }
    
    private static final DateTimeFormatter MESSAGE_CREATED_AT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private CoachingEntryDto.ResponseMessageItem toMessageItem(SessionMessage sessionMessage) {
        return CoachingEntryDto.ResponseMessageItem.builder()
                .messageId(sessionMessage.getMessageId())
                .role(sessionMessage.getRole())
                .message(sessionMessage.getMessage())
                .createdAt(sessionMessage.getCreatedAt().format(MESSAGE_CREATED_AT_FORMATTER))
                .build();
    }
}