package kr.co.mapspring.chat.service.impl;

import kr.co.mapspring.chat.dto.ChatMessageDto;
import kr.co.mapspring.chat.service.ChatService;
import kr.co.mapspring.global.exception.chat.InvalidChatMessageException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;

    private final Set<Long> participantUserIds = ConcurrentHashMap.newKeySet();

    private final Map<String, Long> sessionUserMap = new ConcurrentHashMap<>();

    @Override
    public ChatMessageDto.ResponseMessage enter(String sessionId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        participantUserIds.add(user.getUserId());
        sessionUserMap.put(sessionId, user.getUserId());

        return ChatMessageDto.ResponseMessage.enter(user);
    }

    @Override
    public ChatMessageDto.ResponseMessage sendMessage(Long userId, String message) {
        validateMessage(message);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return ChatMessageDto.ResponseMessage.chat(user, message);
    }

    @Override
    public ChatMessageDto.ResponseMessage leave(String sessionId) {
        Long userId = sessionUserMap.remove(sessionId);

        if (userId == null) {
            return null;
        }

        participantUserIds.remove(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return ChatMessageDto.ResponseMessage.leave(user);
    }

    @Override
    public ChatMessageDto.ResponseParticipantCount getParticipantCount() {
        return ChatMessageDto.ResponseParticipantCount.from(participantUserIds.size());
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new InvalidChatMessageException();
        }
    }
}