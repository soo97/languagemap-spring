package kr.co.mapspring.ai.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FastApiYoutubeDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 유튜브 검색 요청 DTO")
    public static class RequestYoutubeSearch {

        private String keyword;
        private Integer maxResults;

        @Builder
        public RequestYoutubeSearch(String keyword, Integer maxResults) {
            this.keyword = keyword;
            this.maxResults = maxResults;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 유튜브 검색 응답 DTO")
    public static class ResponseYoutubeSearch {

        private List<YoutubeVideoItem> youtubePicks;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 유튜브 영상 항목 DTO")
    public static class YoutubeVideoItem {

        private String title;
        private String channelTitle;
        private String thumbnailUrl;
        private String videoUrl;
        private String description;
        private String summary;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 영상 요약 요청 DTO")
    public static class RequestVideoSummary {

        private String title;
        private String channelTitle;
        private String description;

        @Builder
        public RequestVideoSummary(
                String title,
                String channelTitle,
                String description
        ) {
            this.title = title;
            this.channelTitle = channelTitle;
            this.description = description;
        }
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "FastAPI 영상 요약 응답 DTO")
    public static class ResponseVideoSummary {

        private String summary;
    }
}