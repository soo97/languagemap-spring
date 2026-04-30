package kr.co.mapspring.chat.controller;

import kr.co.mapspring.chat.controller.docs.ChatControllerDocs;
import kr.co.mapspring.chat.dto.ChatMessageDto;
import kr.co.mapspring.global.exception.chat.InvalidChatMessageException;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Controller
@RequiredArgsConstructor
public class ChatController implements ChatControllerDocs {

    private final UserRepository userRepository;

    @Override
    @MessageMapping("/chat/send")
    @SendTo("/topic/chat")
    public ChatMessageDto.ResponseMessage sendMessage(
            ChatMessageDto.RequestSendMessage request
    ) {
        validateMessage(request.getMessage());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return ChatMessageDto.ResponseMessage.of(user, request.getMessage());
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new InvalidChatMessageException();
        }
    }
}