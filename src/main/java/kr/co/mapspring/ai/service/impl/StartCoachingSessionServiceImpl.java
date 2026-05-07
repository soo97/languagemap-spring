package kr.co.mapspring.ai.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.StartCoachingSessionService;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StartCoachingSessionServiceImpl implements StartCoachingSessionService {

    private final CoachingSessionRepository coachingSessionRepository;
    private final LearningSessionRepository learningSessionRepository;

    @Override
    @Transactional
    public StartCoachingSessionDto.ResponseStartCoachingSession startCoachingSession(
            StartCoachingSessionDto.RequestStartCoachingSession request
    ) {
        Long sessionId = request.getSessionId();

        LearningSession learningSession = learningSessionRepository.findBySessionId(sessionId)
                .orElseThrow(LearningSessionNotFoundException::new);

        CoachingSession coachingSession = coachingSessionRepository
                .findByLearningSession_SessionIdAndCoachingSessionStatus(
                        sessionId,
                        CoachingSessionStatus.RUNNING
                )
                .orElseGet(() -> coachingSessionRepository.save(
                        CoachingSession.start(learningSession, request.getOptionType())
                ));

        if (coachingSession.getSelectedOption() == null && request.getOptionType() != null) {
            coachingSession.updateSelectedOption(request.getOptionType());
        }

        return StartCoachingSessionDto.ResponseStartCoachingSession.builder()
                .coachingSessionId(coachingSession.getCoachingSessionId())
                .sessionId(coachingSession.getLearningSession().getSessionId())
                .coachingSessionStatus(coachingSession.getCoachingSessionStatus().name())
                .selectedOption(coachingSession.getSelectedOption())
                .currentTurnOrder(coachingSession.getCurrentTurnOrder())
                .build();
    }
}