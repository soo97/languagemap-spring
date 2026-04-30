package kr.co.mapspring.chat.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.chat.dto.ChatMessageDto;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@Tag(name = "Chat", description = "실시간 전체 채팅 WebSocket API")
public interface ChatControllerDocs {

    @Operation(
            summary = "실시간 채팅방 입장",
            description = """
                    사용자가 커뮤니티 페이지에 진입하면 실시간 전체 채팅방에 자동 입장합니다.

                    WebSocket 연결 주소: /ws
                    메시지 전송 주소: /app/chat/enter
                    채팅 구독 주소: /topic/chat
                    참여자 수 구독 주소: /topic/chat/participants

                    STOMP CONNECT 시 Authorization 헤더에 JWT를 전달해야 합니다.
                    예: Authorization: Bearer {accessToken}

                    응답 메시지 타입: ENTER
                    """
    )
    void enter(
            ChatMessageDto.RequestChatUser request,
            SimpMessageHeaderAccessor headerAccessor
    );

    @Operation(
            summary = "실시간 채팅 메시지 전송",
            description = """
                    사용자가 커뮤니티 페이지에서 메시지를 전송합니다.

                    WebSocket 연결 주소: /ws
                    메시지 전송 주소: /app/chat/send
                    채팅 구독 주소: /topic/chat

                    Request Body에는 message만 전달합니다.
                    userId는 JWT에서 추출합니다.

                    응답 메시지 타입: CHAT
                    """
    )
    void sendMessage(
            ChatMessageDto.RequestSendMessage request,
            SimpMessageHeaderAccessor headerAccessor
    );

    @Operation(
            summary = "실시간 채팅방 퇴장",
            description = """
                    사용자가 커뮤니티 페이지를 벗어나면 실시간 전체 채팅방에서 퇴장합니다.

                    WebSocket 연결 주소: /ws
                    메시지 전송 주소: /app/chat/leave
                    채팅 구독 주소: /topic/chat
                    참여자 수 구독 주소: /topic/chat/participants

                    브라우저 종료, 새로고침 등으로 WebSocket 연결이 끊기면
                    서버에서 자동으로 퇴장 처리합니다.

                    응답 메시지 타입: LEAVE
                    """
    )
    void leave(SimpMessageHeaderAccessor headerAccessor);
}