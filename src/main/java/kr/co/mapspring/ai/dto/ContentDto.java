package kr.co.mapspring.ai.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.ai.entity.Content;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ContentDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "콘텐츠 응답 DTO")
    public static class ResponseContent {

        @Schema(description = "검색 키워드", example = "coffee english conversation")
        private String keyword;

        @Schema(description = "영상 제목", example = "English Conversation at a Coffee Shop")
        private String videoTitle;

        @Schema(description = "채널 이름", example = "Speak English With Me")
        private String channelTitle;

        @Schema(description = "썸네일 URL", example = "https://img.youtube.com/xxx.jpg")
        private String thumbnailUrl;

        @Schema(description = "영상 URL", example = "https://youtube.com/watch?v=xxx")
        private String videoUrl;

        @Schema(description = "영상 요약", example = "이 영상은 카페에서 사용하는 영어 표현을 연습할 수 있습니다.")
        private String videoSummary;

        @Builder
        public ResponseContent(
                String keyword,
                String videoTitle,
                String channelTitle,
                String thumbnailUrl,
                String videoUrl,
                String videoSummary
        ) {
            this.keyword = keyword;
            this.videoTitle = videoTitle;
            this.channelTitle = channelTitle;
            this.thumbnailUrl = thumbnailUrl;
            this.videoUrl = videoUrl;
            this.videoSummary = videoSummary;
        }

        public static ResponseContent from(Content content) {
            return ResponseContent.builder()
                    .keyword(content.getKeyword())
                    .videoTitle(content.getVideoTitle())
                    .channelTitle(content.getChannelTitle())
                    .thumbnailUrl(content.getThumbnailUrl())
                    .videoUrl(content.getVideoUrl())
                    .videoSummary(content.getVideoSummary())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "콘텐츠 목록 응답 DTO")
    public static class ResponseGetContents {

        @Schema(description = "AI 코칭 세션 ID", example = "1")
        private Long coachingSessionId;

        @Schema(description = "추천 콘텐츠 목록")
        private List<ResponseContent> contents;
    }
}