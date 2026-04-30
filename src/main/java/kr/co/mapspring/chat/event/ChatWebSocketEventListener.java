package kr.co.mapspring.chat.event;

import kr.co.mapspring.chat.dto.ChatMessageDto;
import kr.co.mapspring.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class ChatWebSocketEventListener {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        ChatMessageDto.ResponseMessage response = chatService.leave(sessionId);

        if (response == null) {
            return;
        }

        messagingTemplate.convertAndSend("/topic/chat", response);
        messagingTemplate.convertAndSend(
                "/topic/chat/participants",
                chatService.getParticipantCount()
        );
    }
}
