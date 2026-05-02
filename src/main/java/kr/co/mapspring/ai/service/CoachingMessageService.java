package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.CoachingMessageDto;

public interface CoachingMessageService {

	// 요청 DTO 기반으로 사용자 또는 AI 코칭 메시지를 저장
    CoachingMessageDto.ResponseCoachingMessage saveCoachingMessage(
            CoachingMessageDto.RequestSaveCoachingMessage request
    );

    // 코칭 세션 ID를 기준으로 저장된 메시지 목록을 생성 시간 오름차순으로 조회	
    CoachingMessageDto.ResponseGetCoachingMessages getCoachingMessages(Long coachingSessionId);
    
    // 사용자 음성 인식 결과와 음성 URL을 USER 메시지로 저장
    CoachingMessageDto.ResponseCoachingMessage saveUserMessage(
            Long coachingSessionId,
            String message,
            String audioUrl
    );
    // AI 응답 문장과 TTS 음성 URL을 ASSISTANT 메시지로 저장
    CoachingMessageDto.ResponseCoachingMessage saveAssistantMessage(
            Long coachingSessionId,
            String message,
            String audioUrl
    );
}