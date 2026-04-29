package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.CoachingMessageDto;

public interface CoachingMessageService {

    CoachingMessageDto.ResponseCoachingMessage saveCoachingMessage(
            CoachingMessageDto.RequestSaveCoachingMessage request
    );

    CoachingMessageDto.ResponseGetCoachingMessages getCoachingMessages(Long coachingSessionId);
}