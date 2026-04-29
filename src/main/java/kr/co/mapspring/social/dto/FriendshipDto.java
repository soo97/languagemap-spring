package kr.co.mapspring.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FriendshipDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "친구 요청 전송 요청 DTO")
    public static class RequestSendFriendRequest {

        @Schema(description = "요청 보내는 사용자 ID", example = "1")
        private Long requesterId;

        @Schema(description = "요청 받는 사용자 ID", example = "2")
        private Long addresseeId;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "이메일 친구 요청 DTO")
    public static class RequestSendFriendRequestByEmail {

        @Schema(description = "요청 보내는 사용자 ID", example = "1")
        private Long requesterId;

        @Schema(description = "요청 받는 사용자 이메일", example = "user2@test.com")
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "친구 요청 처리 요청 DTO")
    public static class RequestHandleFriendRequest {

        @Schema(description = "사용자 ID", example = "2")
        private Long userId;
    }

    @Getter
    @Builder
    @Schema(description = "친구 정보 응답 DTO")
    public static class ResponseFriend {

        @Schema(description = "친구 관계 ID", example = "1")
        private Long friendshipId;

        @Schema(description = "요청 보낸 사용자 ID", example = "1")
        private Long requesterId;

        @Schema(description = "요청 받은 사용자 ID", example = "2")
        private Long addresseeId;

        @Schema(description = "친구 상태 (PENDING, ACCEPTED, REJECTED)", example = "ACCEPTED")
        private FriendshipStatus status;

        public static ResponseFriend from(Friendship friendship) {
            return ResponseFriend.builder()
                    .friendshipId(friendship.getFriendshipId())
                    .requesterId(friendship.getRequester().getUserId())
                    .addresseeId(friendship.getAddressee().getUserId())
                    .status(friendship.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "추천 친구 응답 DTO")
    public static class ResponseRecommendedFriend {

        @Schema(description = "사용자 ID", example = "2")
        private Long userId;

        @Schema(description = "사용자 이름", example = "유저2")
        private String name;

        @Schema(description = "이메일", example = "user2@test.com")
        private String email;

        public static ResponseRecommendedFriend from(User user) {
            return ResponseRecommendedFriend.builder()
                    .userId(user.getUserId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }
    }

}
