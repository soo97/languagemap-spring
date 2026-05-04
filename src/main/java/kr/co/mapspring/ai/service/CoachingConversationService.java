package kr.co.mapspring.ai.service;

import org.springframework.web.multipart.MultipartFile;

import kr.co.mapspring.ai.dto.CoachingConversationDto;

public interface CoachingConversationService {

    CoachingConversationDto.ResponsePrepareScript prepareScript(
            CoachingConversationDto.RequestPrepareScript request
    );

    CoachingConversationDto.ResponseStartConversation startConversation(
            Long coachingSessionId
    );

    CoachingConversationDto.ResponseConversationTurn processUserSpeech(
            Long coachingSessionId,
            MultipartFile audioFile
    );

    CoachingConversationDto.ResponseFinishConversation finishConversation(
            Long coachingSessionId
    );
}