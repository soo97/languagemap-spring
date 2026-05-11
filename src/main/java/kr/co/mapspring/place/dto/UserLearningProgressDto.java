package kr.co.mapspring.place.dto;

import java.util.List;

import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.MissionSessionStatus;
import lombok.Builder;
import lombok.Getter;

public class UserLearningProgressDto {

    @Getter
    @Builder
    public static class Response {

        private Long placeId;
        private Long learningSessionId;
        private String level;

        private Long activeMissionId;
        private List<Long> completedMissionIds;
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
    public static class Message {
        private String role;
        private String speaker;
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
