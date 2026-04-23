package kr.co.mapspring.ai.dto;

import java.util.List;

import kr.co.mapspring.place.enums.SessionMessageRole;
import lombok.Builder;
import lombok.Getter;

public class CoachingEntryDto {

    @Getter
    @Builder
    public static class ResponseGetCoachingEntry {
        private Long sessionId;
        private Long placeId;
        private String placeName;
        private String country;
        private String city;
        private String placeDescription;
        private String evaluation;
        private List<ResponseMessageItem> sessionMessages;
    }

    @Getter
    @Builder
    public static class ResponseMessageItem {
        private Long messageId;
        private SessionMessageRole role;
        private String message;
        private String createdAt;
    }
}