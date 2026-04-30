package kr.co.mapspring.chat.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.chat.dto.ChatMessageDto;

@Tag(name = "Chat", description = "실시간 전체 채팅 WebSocket API")
public interface ChatControllerDocs {

    @Operation(
            summary = "실시간 채팅 메시지 전송",
            description = """
                    사용자가 커뮤니티 채팅방에서 메시지를 전송합니다.

                    WebSocket 연결 주소: /ws
                    메시지 전송 주소: /app/chat/send
                    메시지 구독 주소: /topic/chat
                    """
    )
    ChatMessageDto.ResponseMessage sendMessage(
            ChatMessageDto.RequestSendMessage request
    );
}