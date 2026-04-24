package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.StartCoachingSessionDto;

public interface StartCoachingSessionService {

    StartCoachingSessionDto.ResponseStartCoachingSession startCoachingSession(
            StartCoachingSessionDto.RequestStartCoachingSession request
    );
}