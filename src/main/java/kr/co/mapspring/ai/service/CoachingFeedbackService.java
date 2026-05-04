package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.CoachingFeedbackDto;

public interface CoachingFeedbackService {

    // AI 코칭 최종 피드백을 저장한다.
    CoachingFeedbackDto.ResponseCoachingFeedback saveCoachingFeedback(
            CoachingFeedbackDto.RequestSaveCoachingFeedback request
    );

    // 코칭 세션 ID를 기준으로 최종 피드백을 조회한다.
    CoachingFeedbackDto.ResponseCoachingFeedback getCoachingFeedback(Long coachingSessionId);
}