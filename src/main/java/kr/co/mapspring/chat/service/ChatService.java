package kr.co.mapspring.chat.service;

import kr.co.mapspring.chat.dto.ChatMessageDto;

public interface ChatService {

    /**
     * 사용자가 채팅방에 입장한다.
     *
     * 입장 시 참여자 수가 증가하며,
     * 전체 사용자에게 입장 메시지를 전송한다.
     *
     * @param sessionId WebSocket 세션 ID
     * @param userId 사용자 ID
     * @return 입장 메시지 응답 DTO
     */
    ChatMessageDto.ResponseMessage enter(String sessionId, Long userId);

    /**
     * 사용자가 채팅 메시지를 전송한다.
     *
     * 메시지는 비어 있을 수 없다.
     *
     * @param userId 사용자 ID
     * @param message 메시지 내용
     * @return 채팅 메시지 응답 DTO
     */
    ChatMessageDto.ResponseMessage sendMessage(Long userId, String message);

    /**
     * 사용자가 채팅방에서 퇴장한다.
     *
     * 퇴장 시 참여자 수가 감소하며,
     * 전체 사용자에게 퇴장 메시지를 전송한다.
     *
     * @param sessionId WebSocket 세션 ID
     * @return 퇴장 메시지 응답 DTO
     */
    ChatMessageDto.ResponseMessage leave(String sessionId);

    /**
     * 현재 채팅방 참여자 수를 조회한다.
     *
     * @return 참여자 수 응답 DTO
     */
    ChatMessageDto.ResponseParticipantCount getParticipantCount();

}
