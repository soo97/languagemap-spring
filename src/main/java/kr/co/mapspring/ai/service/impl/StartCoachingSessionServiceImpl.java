package kr.co.mapspring.ai.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.StartCoachingSessionService;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StartCoachingSessionServiceImpl implements StartCoachingSessionService {

    private final CoachingSessionRepository coachingSessionRepository;
    private final LearningSessionRepository learningSessionRepository;

    @Override
    public StartCoachingSessionDto.ResponseStartCoachingSession startCoachingSession(
            StartCoachingSessionDto.RequestStartCoachingSession request) {

        Long sessionId = request.getSessionId();

        LearningSession learningSession = learningSessionRepository.findBySessionId(sessionId)
                .orElseThrow(LearningSessionNotFoundException::new);

        CoachingSession coachingSession = coachingSessionRepository.findByLearningSession_SessionId(sessionId)
                .orElseGet(() -> coachingSessionRepository.save(CoachingSession.start(learningSession)));

        return StartCoachingSessionDto.ResponseStartCoachingSession.builder()
                .coachingSessionId(coachingSession.getCoachingSessionId())
                .sessionId(coachingSession.getLearningSession().getSessionId())
                .coachingSessionStatus(coachingSession.getCoachingSessionStatus().name())
                .optionType(request.getOptionType())
                .build();
    }
}