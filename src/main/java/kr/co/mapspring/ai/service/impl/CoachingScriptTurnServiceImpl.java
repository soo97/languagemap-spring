package kr.co.mapspring.ai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.CoachingScriptTurnDto;
import kr.co.mapspring.ai.entity.CoachingScriptTurn;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.repository.CoachingScriptTurnRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.CoachingScriptTurnService;
import kr.co.mapspring.global.exception.ai.CoachingScriptTurnNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingScriptTurnServiceImpl implements CoachingScriptTurnService {

    private final CoachingScriptTurnRepository coachingScriptTurnRepository;
    private final CoachingSessionRepository coachingSessionRepository;

    @Override
    @Transactional
    public CoachingScriptTurnDto.ResponseCoachingScriptTurn saveScriptTurn(
            CoachingScriptTurnDto.RequestSaveCoachingScriptTurn request
    ) {
        CoachingSession coachingSession = coachingSessionRepository.findById(request.getCoachingSessionId())
                .orElseThrow(CoachingSessionNotFoundException::new);

        CoachingScriptTurn scriptTurn = CoachingScriptTurn.create(
                coachingSession,
                request.getTurnOrder(),
                request.getAssistantText(),
                request.getExpectedText()
        );

        CoachingScriptTurn saved = coachingScriptTurnRepository.save(scriptTurn);

        return CoachingScriptTurnDto.ResponseCoachingScriptTurn.from(saved);
    }

    @Override
    @Transactional
    public List<CoachingScriptTurnDto.ResponseCoachingScriptTurn> saveScriptTurns(
            Long coachingSessionId,
            List<CoachingScriptTurnDto.RequestSaveCoachingScriptTurn> requests
    ) {
        CoachingSession coachingSession = coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        List<CoachingScriptTurn> scriptTurns = requests.stream()
                .map(request -> CoachingScriptTurn.create(
                        coachingSession,
                        request.getTurnOrder(),
                        request.getAssistantText(),
                        request.getExpectedText()
                ))
                .toList();

        return coachingScriptTurnRepository.saveAll(scriptTurns)
                .stream()
                .map(CoachingScriptTurnDto.ResponseCoachingScriptTurn::from)
                .toList();
    }

    @Override
    public CoachingScriptTurnDto.ResponseCoachingScriptTurn getScriptTurn(
            Long coachingSessionId,
            Integer turnOrder
    ) {
        CoachingScriptTurn scriptTurn = coachingScriptTurnRepository
                .findByCoachingSession_CoachingSessionIdAndTurnOrder(coachingSessionId, turnOrder)
                .orElseThrow(CoachingScriptTurnNotFoundException::new);

        return CoachingScriptTurnDto.ResponseCoachingScriptTurn.from(scriptTurn);
    }

    @Override
    public CoachingScriptTurnDto.ResponseGetCoachingScriptTurns getScriptTurns(Long coachingSessionId) {
        coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        List<CoachingScriptTurnDto.ResponseCoachingScriptTurn> turns =
                coachingScriptTurnRepository
                        .findByCoachingSession_CoachingSessionIdOrderByTurnOrderAsc(coachingSessionId)
                        .stream()
                        .map(CoachingScriptTurnDto.ResponseCoachingScriptTurn::from)
                        .toList();

        return CoachingScriptTurnDto.ResponseGetCoachingScriptTurns.builder()
                .coachingSessionId(coachingSessionId)
                .turns(turns)
                .build();
    }
}