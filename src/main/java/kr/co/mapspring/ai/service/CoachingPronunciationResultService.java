package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.CoachingPronunciationResultDto;

public interface CoachingPronunciationResultService {

    // 발음 평가 결과를 저장
    CoachingPronunciationResultDto.ResponsePronunciationResult savePronunciationResult(
            CoachingPronunciationResultDto.RequestSavePronunciationResult request
    );

    // 메시지 ID를 기준으로 발음 평가 결과를 조회
    CoachingPronunciationResultDto.ResponsePronunciationResult getPronunciationResultByMessageId(
            Long coachingMessageId
    );

    // 코칭 세션 ID를 기준으로 발음 평가 결과 목록을 조회한
    CoachingPronunciationResultDto.ResponseGetPronunciationResults getPronunciationResults(
            Long coachingSessionId
    );
}