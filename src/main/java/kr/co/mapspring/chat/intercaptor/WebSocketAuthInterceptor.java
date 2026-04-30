package kr.co.mapspring.chat.interceptor;

import kr.co.mapspring.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader =
                    accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);

            if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
                throw new IllegalArgumentException("Access Token이 필요합니다.");
            }

            String token = authorizationHeader.substring(BEARER_PREFIX.length());

            if (!jwtTokenProvider.validateAccessToken(token)) {
                throw new IllegalArgumentException("유효하지 않은 Access Token입니다.");
            }

            Long userId = jwtTokenProvider.getUserId(token);

            accessor.setUser(new ChatPrincipal(userId));
        }

        return message;
    }

    private static class ChatPrincipal implements Principal {

        private final Long userId;

        private ChatPrincipal(Long userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return String.valueOf(userId);
        }
    }
}