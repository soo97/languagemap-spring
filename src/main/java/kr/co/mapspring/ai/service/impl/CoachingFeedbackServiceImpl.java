package kr.co.mapspring.ai.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingFeedbackDto;
import kr.co.mapspring.ai.entity.CoachingFeedback;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.repository.CoachingFeedbackRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.CoachingFeedbackService;
import kr.co.mapspring.global.exception.ai.CoachingFeedbackAlreadyExistsException;
import kr.co.mapspring.global.exception.ai.CoachingFeedbackNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingFeedbackServiceImpl implements CoachingFeedbackService {

    private final CoachingFeedbackRepository coachingFeedbackRepository;
    private final CoachingSessionRepository coachingSessionRepository;

    @Override
    @Transactional
    public CoachingFeedbackDto.ResponseCoachingFeedback saveCoachingFeedback(
            CoachingFeedbackDto.RequestSaveCoachingFeedback request
    ) {
        CoachingSession coachingSession = coachingSessionRepository.findById(request.getCoachingSessionId())
                .orElseThrow(CoachingSessionNotFoundException::new);

        if (coachingFeedbackRepository.existsByCoachingSession_CoachingSessionId(
                request.getCoachingSessionId()
        )) {
            throw new CoachingFeedbackAlreadyExistsException();
        }

        CoachingFeedback coachingFeedback = CoachingFeedback.create(
                coachingSession,
                request.getTotalScore(),
                request.getSummaryFeedback(),
                request.getNaturalnessLevel(),
                request.getNaturalnessComment(),
                request.getFlowLevel(),
                request.getFlowComment(),
                request.getPronunciationLevel(),
                request.getPronunciationComment(),
                request.getProblemWords()
        );

        CoachingFeedback saved = coachingFeedbackRepository.save(coachingFeedback);

        return CoachingFeedbackDto.ResponseCoachingFeedback.from(saved);
    }

    @Override
    public CoachingFeedbackDto.ResponseCoachingFeedback getCoachingFeedback(Long coachingSessionId) {
        coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        CoachingFeedback coachingFeedback = coachingFeedbackRepository
                .findByCoachingSession_CoachingSessionId(coachingSessionId)
                .orElseThrow(CoachingFeedbackNotFoundException::new);

        return CoachingFeedbackDto.ResponseCoachingFeedback.from(coachingFeedback);
    }
}