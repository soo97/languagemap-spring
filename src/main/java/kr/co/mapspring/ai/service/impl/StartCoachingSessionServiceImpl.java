package kr.co.mapspring.ai.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.StartCoachingSessionService;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.global.exception.ai.AiCoachingAccessDeniedException;
import kr.co.mapspring.payment.service.SubscriptionValidator;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StartCoachingSessionServiceImpl implements StartCoachingSessionService {

    private final CoachingSessionRepository coachingSessionRepository;
    private final LearningSessionRepository learningSessionRepository;
    private final SubscriptionValidator subscriptionValidator;

    @Override
    @Transactional
    public StartCoachingSessionDto.ResponseStartCoachingSession startCoachingSession(
            StartCoachingSessionDto.RequestStartCoachingSession request
    ) {
        Long sessionId = request.getSessionId();

        log.info(
                "Start coaching session request received. sessionId={}, optionType={}",
                sessionId,
                request.getOptionType()
        );

        LearningSession learningSession = learningSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> {
                    log.warn("Learning session not found for coaching start. sessionId={}", sessionId);
                    return new LearningSessionNotFoundException();
                });

        log.info("Learning session found for coaching start. sessionId={}", sessionId);
        
        Long userId = learningSession.getUser().getUserId();
        log.info(
                "Coaching start user resolved. sessionId={}, userId={}, optionType={}",
                sessionId,
                userId,
                request.getOptionType()
        );

        boolean aiCoachingAccessAllowed = subscriptionValidator.isPremium(userId);
        log.info(
                "AI coaching access checked. sessionId={}, userId={}, optionType={}, aiCoachingAccessAllowed={}",
                sessionId,
                userId,
                request.getOptionType(),
                aiCoachingAccessAllowed
        );

        if (!aiCoachingAccessAllowed) {
            throw new AiCoachingAccessDeniedException();
        }

        Optional<CoachingSession> existingCoachingSession =
                coachingSessionRepository.findByLearningSession_SessionIdAndCoachingSessionStatus(
                                sessionId,
                                CoachingSessionStatus.RUNNING
                        );

        boolean reusedCoachingSession = existingCoachingSession.isPresent();
        CoachingSession coachingSession = existingCoachingSession
                .orElseGet(() -> coachingSessionRepository.save(
                        CoachingSession.start(learningSession, request.getOptionType())
                ));

        log.info(
                "Coaching session resolved. sessionId={}, userId={}, coachingSessionId={}, reusedCoachingSession={}, status={}",
                sessionId,
                userId,
                coachingSession.getCoachingSessionId(),
                reusedCoachingSession,
                coachingSession.getCoachingSessionStatus()
        );

        if (coachingSession.getSelectedOption() == null && request.getOptionType() != null) {
            coachingSession.updateSelectedOption(request.getOptionType());
        }

        log.info(
                "Coaching session response ready. sessionId={}, userId={}, coachingSessionId={}, optionType={}",
                sessionId,
                userId,
                coachingSession.getCoachingSessionId(),
                coachingSession.getSelectedOption()
        );

        return StartCoachingSessionDto.ResponseStartCoachingSession.builder()
                .coachingSessionId(coachingSession.getCoachingSessionId())
                .sessionId(coachingSession.getLearningSession().getSessionId())
                .userId(coachingSession.getLearningSession().getUser().getUserId())
                .coachingSessionStatus(coachingSession.getCoachingSessionStatus().name())
                .selectedOption(coachingSession.getSelectedOption())
                .currentTurnOrder(coachingSession.getCurrentTurnOrder())
                .build();
    }
}
