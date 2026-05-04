package kr.co.mapspring.ai.service;

import java.util.List;

import kr.co.mapspring.ai.dto.CoachingScriptTurnDto;

public interface CoachingScriptTurnService {

    // 코칭 스크립트 턴 하나를 저장
    CoachingScriptTurnDto.ResponseCoachingScriptTurn saveScriptTurn(
            CoachingScriptTurnDto.RequestSaveCoachingScriptTurn request
    );

    // FastAPI에서 받은 코칭 스크립트 턴 목록을 한 번에 저장 
    List<CoachingScriptTurnDto.ResponseCoachingScriptTurn> saveScriptTurns(
            Long coachingSessionId,
            List<CoachingScriptTurnDto.RequestSaveCoachingScriptTurn> requests
    );

    // 코칭 세션 ID와 턴 순서로 특정 스크립트 턴을 조회
    CoachingScriptTurnDto.ResponseCoachingScriptTurn getScriptTurn(
            Long coachingSessionId,
            Integer turnOrder
    );

    // 코칭 세션 ID를 기준으로 전체 스크립트 턴을 턴 순서 오름차순으로 조회
    CoachingScriptTurnDto.ResponseGetCoachingScriptTurns getScriptTurns(Long coachingSessionId);
}