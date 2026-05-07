package kr.co.mapspring.chat.controller;

import kr.co.mapspring.chat.controller.docs.ChatControllerDocs;
import kr.co.mapspring.chat.dto.ChatMessageDto;
import kr.co.mapspring.chat.service.ChatService;
import kr.co.mapspring.chat.session.ChatSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class  ChatController implements ChatControllerDocs {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatSessionRegistry chatSessionRegistry;

    @Override
    @MessageMapping("/chat/enter")
    public void enter(
            ChatMessageDto.RequestChatUser request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String sessionId = headerAccessor.getSessionId();
        Long userId = getUserId(sessionId);

        ChatMessageDto.ResponseMessage response =
                chatService.enter(sessionId, userId);

        if (response == null) {
            return;
        }

        messagingTemplate.convertAndSend("/topic/chat", response);
        messagingTemplate.convertAndSend(
                "/topic/chat/participants",
                chatService.getParticipantCount()
        );
    }

    @Override
    @MessageMapping("/chat/send")
    public void sendMessage(
            ChatMessageDto.RequestSendMessage request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String sessionId = headerAccessor.getSessionId();
        Long userId = getUserId(sessionId);

        ChatMessageDto.ResponseMessage response =
                chatService.sendMessage(userId, request.getMessage());

        messagingTemplate.convertAndSend("/topic/chat", response);
    }

    @Override
    @MessageMapping("/chat/leave")
    public void leave(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        ChatMessageDto.ResponseMessage response = chatService.leave(sessionId);

        chatSessionRegistry.remove(sessionId);

        if (response == null) {
            return;
        }

        messagingTemplate.convertAndSend("/topic/chat", response);
        messagingTemplate.convertAndSend(
                "/topic/chat/participants",
                chatService.getParticipantCount()
        );
    }

    private Long getUserId(String sessionId) {
        Long userId = chatSessionRegistry.getUserId(sessionId);

        if (userId == null) {
            throw new IllegalArgumentException("인증된 사용자 정보가 없습니다.");
        }

        return userId;
    }
}