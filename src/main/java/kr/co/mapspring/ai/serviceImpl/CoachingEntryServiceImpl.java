package kr.co.mapspring.ai.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingEntryDto;
import kr.co.mapspring.ai.service.CoachingEntryService;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.SessionEvaluation;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.SessionEvaluationRepository;
import kr.co.mapspring.place.repository.SessionMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingEntryServiceImpl implements CoachingEntryService {

    private final LearningSessionRepository learningSessionRepository;
    private final SessionMessageRepository sessionMessageRepository;
    private final SessionEvaluationRepository sessionEvaluationRepository;

    @Override
    public CoachingEntryDto.Response getCoachingEntryData(Long sessionId) {
        LearningSession learningSession = learningSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("학습 세션을 찾을 수 없습니다. sessionId=" + sessionId));

        Place place = learningSession.getPlace();
        Region region = place.getRegion();

        List<SessionMessage> sessionMessages =
                sessionMessageRepository.findBySession_SessionIdOrderByCreatedAtAsc(sessionId);

        String evaluation = sessionEvaluationRepository.findBySession_SessionId(sessionId)
                .map(SessionEvaluation::getEvaluation)
                .orElse("");

        List<CoachingEntryDto.MessageItem> messageItems = sessionMessages.stream()
                .map(this::toMessageItem)
                .toList();

        return CoachingEntryDto.Response.builder()
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

    private CoachingEntryDto.MessageItem toMessageItem(SessionMessage sessionMessage) {
        return CoachingEntryDto.MessageItem.builder()
                .messageId(sessionMessage.getMessageId())
                .role(sessionMessage.getRole())
                .message(sessionMessage.getMessage())
                .createdAt(sessionMessage.getCreatedAt().toString())
                .build();
    }
}