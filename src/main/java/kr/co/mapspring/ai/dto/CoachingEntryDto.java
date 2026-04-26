package kr.co.mapspring.ai.dto;

import java.util.List;

import kr.co.mapspring.place.enums.SessionMessageRole;
import lombok.Builder;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

public class CoachingEntryDto {

	@Getter
    @Builder
    @Schema(description = "AI 코칭 진입 데이터 응답 DTO")
    public static class ResponseGetCoachingEntry {

        @Schema(description = "지도 학습 세션 ID", example = "1")
        private Long sessionId;

        @Schema(description = "장소 ID", example = "1")
        private Long placeId;

        @Schema(description = "장소 이름", example = "Cafe Stage 888")
        private String placeName;

        @Schema(description = "국가", example = "Australia")
        private String country;

        @Schema(description = "도시", example = "Sydney")
        private String city;

        @Schema(description = "장소 주소", example = "Near George St.")
        private String placeAddress;

        @Schema(description = "지도 학습 평가", example = "발음 보통, 표현 좋음, 속도 개선 필요")
        private String evaluation;

        @Schema(description = "지도 학습 대화 메시지 목록")
        private List<ResponseMessageItem> sessionMessages;
    }

    @Getter
    @Builder
    @Schema(description = "지도 학습 메시지 응답 DTO")
    public static class ResponseMessageItem {

        @Schema(description = "지도 학습 메시지 ID", example = "1")
        private Long messageId;

        @Schema(description = "메시지 역할", example = "USER")
        private SessionMessageRole role;

        @Schema(description = "메시지 내용", example = "I would like a latte with almond milk, please.")
        private String message;

        @Schema(description = "메시지 생성 시각", example = "2026-04-26 16:20:31")
        private String createdAt;
    }
}