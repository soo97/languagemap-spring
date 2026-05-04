package kr.co.mapspring.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.chat.enums.ChatMessageType;
import kr.co.mapspring.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChatMessageDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "채팅 메시지 전송 요청 DTO")
    public static class RequestSendMessage {

        @Schema(description = "메시지 내용", example = "안녕하새요~!")
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "채팅방 입장/퇴장 요청 DTO")
    public static class RequestChatUser {
    }

    @Getter
    @Builder
    @Schema(description = "채팅 메시지 응답 DTO")
    public static class ResponseMessage {

        @Schema(description = "메시지 타입", example = "CHAT")
        private ChatMessageType type;

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "닉네임", example = "홍길동")
        private String nickname;

        @Schema(description = "메시지 내용", example = "방가방가~!")
        private String message;

        @Schema(description = "전송 시간", example = "2026-04-30T10:00:00")
        private LocalDateTime sentAt;

        public static ResponseMessage chat(User user, String message) {
            return ResponseMessage.builder()
                    .type(ChatMessageType.CHAT)
                    .userId(user.getUserId())
                    .nickname(user.getName())
                    .message(message)
                    .sentAt(LocalDateTime.now())
                    .build();
        }

        public static ResponseMessage enter(User user) {
            return ResponseMessage.builder()
                    .type(ChatMessageType.ENTER)
                    .userId(user.getUserId())
                    .nickname(user.getName())
                    .message(user.getName() + "님이 입장했습니다.")
                    .sentAt(LocalDateTime.now())
                    .build();
        }

        public static ResponseMessage leave(User user) {
            return ResponseMessage.builder()
                    .type(ChatMessageType.LEAVE)
                    .userId(user.getUserId())
                    .nickname(user.getName())
                    .message(user.getName() + "님이 퇴장했습니다.")
                    .sentAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "채팅 참여자 수 응답 DTO")
    public static class ResponseParticipantCount {

        @Schema(description = "현재 참여자 수", example = "12")
        private int count;

        public static ResponseParticipantCount from(int count) {
            return ResponseParticipantCount.builder()
                    .count(count)
                    .build();
        }
    }
}
