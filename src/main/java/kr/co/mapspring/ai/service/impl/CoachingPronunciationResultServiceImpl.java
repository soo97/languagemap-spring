package kr.co.mapspring.ai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingPronunciationResultDto;
import kr.co.mapspring.ai.entity.CoachingMessage;
import kr.co.mapspring.ai.entity.CoachingPronunciationResult;
import kr.co.mapspring.ai.entity.CoachingScriptTurn;
import kr.co.mapspring.ai.repository.CoachingMessageRepository;
import kr.co.mapspring.ai.repository.CoachingPronunciationResultRepository;
import kr.co.mapspring.ai.repository.CoachingScriptTurnRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.CoachingPronunciationResultService;
import kr.co.mapspring.global.exception.ai.CoachingMessageNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingPronunciationResultNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingScriptTurnNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingPronunciationResultServiceImpl implements CoachingPronunciationResultService {

    private final CoachingPronunciationResultRepository pronunciationResultRepository;
    private final CoachingMessageRepository coachingMessageRepository;
    private final CoachingScriptTurnRepository coachingScriptTurnRepository;
    private final CoachingSessionRepository coachingSessionRepository;

    @Override
    @Transactional
    public CoachingPronunciationResultDto.ResponsePronunciationResult savePronunciationResult(
            CoachingPronunciationResultDto.RequestSavePronunciationResult request
    ) {
        CoachingMessage coachingMessage = coachingMessageRepository.findById(request.getCoachingMessageId())
                .orElseThrow(CoachingMessageNotFoundException::new);

        CoachingScriptTurn coachingScriptTurn = coachingScriptTurnRepository.findById(request.getCoachingScriptTurnId())
                .orElseThrow(CoachingScriptTurnNotFoundException::new);

        CoachingPronunciationResult pronunciationResult = CoachingPronunciationResult.create(
                coachingMessage,
                coachingScriptTurn,
                request.getRecognizedText(),
                request.getAccuracyScore(),
                request.getFluencyScore(),
                request.getCompletenessScore(),
                request.getPronunciationScore(),
                request.getFeedback(),
                request.getProblemWords()
        );

        CoachingPronunciationResult saved = pronunciationResultRepository.save(pronunciationResult);

        return CoachingPronunciationResultDto.ResponsePronunciationResult.from(saved);
    }

    @Override
    public CoachingPronunciationResultDto.ResponsePronunciationResult getPronunciationResultByMessageId(
            Long coachingMessageId
    ) {
        CoachingPronunciationResult result = pronunciationResultRepository
                .findByCoachingMessage_CoachingMessageId(coachingMessageId)
                .orElseThrow(CoachingPronunciationResultNotFoundException::new);

        return CoachingPronunciationResultDto.ResponsePronunciationResult.from(result);
    }

    @Override
    public CoachingPronunciationResultDto.ResponseGetPronunciationResults getPronunciationResults(
            Long coachingSessionId
    ) {
        coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        List<CoachingPronunciationResultDto.ResponsePronunciationResult> results =
                pronunciationResultRepository
                        .findByCoachingScriptTurn_CoachingSession_CoachingSessionIdOrderByCreatedAtAsc(
                                coachingSessionId
                        )
                        .stream()
                        .map(CoachingPronunciationResultDto.ResponsePronunciationResult::from)
                        .toList();

        return CoachingPronunciationResultDto.ResponseGetPronunciationResults.builder()
                .coachingSessionId(coachingSessionId)
                .pronunciationResults(results)
                .build();
    }
}