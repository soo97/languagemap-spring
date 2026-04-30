package kr.co.mapspring.chat.controller;

import kr.co.mapspring.chat.controller.docs.ChatControllerDocs;
import kr.co.mapspring.chat.dto.ChatMessageDto;
import kr.co.mapspring.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController implements ChatControllerDocs {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @MessageMapping("/chat/enter")
    public void enter(
            ChatMessageDto.RequestChatUser request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String sessionId = headerAccessor.getSessionId();
        Long userId = getUserId(headerAccessor.getUser());

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
        Long userId = getUserId(headerAccessor.getUser());

        ChatMessageDto.ResponseMessage response =
                chatService.sendMessage(userId, request.getMessage());

        messagingTemplate.convertAndSend("/topic/chat", response);
    }

    @Override
    @MessageMapping("/chat/leave")
    public void leave(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

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

    private Long getUserId(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }

        return Long.valueOf(principal.getName());
    }
}