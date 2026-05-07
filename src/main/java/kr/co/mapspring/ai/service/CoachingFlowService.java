package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.StartCoachingWithInitialMessageDto;

public interface CoachingFlowService {

    StartCoachingWithInitialMessageDto.ResponseStartCoachingWithInitialMessage startCoachingWithInitialMessage(
            StartCoachingWithInitialMessageDto.RequestStartCoachingWithInitialMessage request
    );
}