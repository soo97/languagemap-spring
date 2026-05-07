package kr.co.mapspring.chat.session;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatSessionRegistry {

    private final Map<String, Long> sessionUserMap = new ConcurrentHashMap<>();

    public void register(String sessionId, Long userId) {
        sessionUserMap.put(sessionId, userId);
    }

    public Long getUserId(String sessionId) {
        return sessionUserMap.get(sessionId);
    }

    public void remove(String sessionId) {
        sessionUserMap.remove(sessionId);
    }
}