package kr.co.mapspring.place.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.MissionSessionStatus;
import lombok.Builder;
import lombok.Getter;

public class UserLearningProgressDto {

	@Getter
    @Builder
    @Schema(
            name = "UserLearningProgressResponse",
            description = "사용자 학습 진행 상황 조회 응답 DTO"
    		)
    public static class Response {

        @Schema(
                description = "장소 ID",
                example = "1"
        		)
        private Long placeId;

        @Schema(
                description = "학습 세션 ID",
                example = "10"
        		)
        private Long learningSessionId;

        @Schema(
                description = "현재 학습 레벨",
                example = "BEGINNER"
        		)
        private String level;

        @Schema(
                description = "현재 진행 중인 미션 ID",
                example = "2",
                nullable = true
        		)
        private Long activeMissionId;

        @Schema(
                description = "완료한 미션 ID 목록",
                example = "[1, 2]"
        		)
        private List<Long> completedMissionIds;

        @Schema(
                description = "저장된 대화 메시지 목록"
        		)
        private List<Message> messages;

        public static Response from(
                LearningSession learningSession,
                List<MissionSession> missionSessions,
                List<SessionMessage> sessionMessages
        ) {
            Long activeMissionId = missionSessions.stream()
                    .filter(ms -> ms.getMissionStatus() == MissionSessionStatus.RUNNING)
                    .map(ms -> ms.getMission().getMissionId())
                    .findFirst()
                    .orElse(null);

            List<Long> completedMissionIds = missionSessions.stream()
                    .filter(ms -> ms.getMissionStatus() == MissionSessionStatus.COMPLETED)
                    .map(ms -> ms.getMission().getMissionId())
                    .toList();

            List<Message> messages = sessionMessages.stream()
                    .map(Message::from)
                    .toList();

            return Response.builder()
                    .placeId(learningSession.getPlace().getPlaceId())
                    .learningSessionId(learningSession.getSessionId())
                    .level(learningSession.getLevel().name())
                    .activeMissionId(activeMissionId)
                    .completedMissionIds(completedMissionIds)
                    .messages(messages)
                    .build();
        }
    }

	@Getter
    @Builder
    @Schema(
            name = "UserLearningProgressMessage",
            description = "사용자 학습 대화 메시지 DTO"
    		)
    public static class Message {

        @Schema(
                description = "메시지 역할",
                example = "ai"
        		)
        private String role;

        @Schema(
                description = "화자 이름",
                example = "AI Coach"
        		)
        private String speaker;

        @Schema(
                description = "대화 메시지 내용",
                example = "카페 학습 세션이 시작되었습니다."
        		)
        private String text;

        public static Message from(SessionMessage message) {
            String role = message.getRole().name().toLowerCase();

            return Message.builder()
                    .role(role.equals("user") ? "user" : "ai")
                    .speaker(role.equals("user") ? "You" : "AI Coach")
                    .text(message.getMessage())
                    .build();
        }
    }
}
