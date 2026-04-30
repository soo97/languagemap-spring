package kr.co.mapspring.support.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.support.entity.Notice;
import kr.co.mapspring.support.enums.NoticeKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NoticeDto {


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "공지사항 단건 상세 응답 DTO")
    public static class ResponseDetail {
    	
        @Schema(description = "공지사항번호", example = "1")
        private Long noticeId;
 
        @Schema(description = "제목", example = "점검 공지")
        private String noticeTitle;
 
        @Schema(description = "유형", example = "CHECK")
        private NoticeKind noticeKind;
 
        @Schema(description = "공지사항 내용", example = "8월 19일날 점검을 하게 되어 접속이 안될 수 있습니다.")
        private String noticeText;
 
        @Schema(description = "이미지 목록")
        private List<String> noticeImg;
 
        @Schema(description = "링크 목록")
        private List<String> noticeUrl;
 
        @Schema(description = "작성일", example = "2026-04-23T10:00:00")
        private LocalDateTime noticeDate;
 
        @Schema(description = "수정일", example = "2026-04-25T10:00:00")
        private LocalDateTime noticeChange;
 
        // Entity -> Response DTO 변환
        public static ResponseDetail from(Notice notice, List<String> noticeImg, List<String> noticeUrl) {
            return ResponseDetail.builder()
                    .noticeId(notice.getNoticeId())
                    .noticeTitle(notice.getNoticeTitle())
                    .noticeKind(notice.getNoticeKind())
                    .noticeText(notice.getNoticeText())
                    .noticeImg(noticeImg)
                    .noticeUrl(noticeUrl)
                    .noticeDate(notice.getNoticeDate())
                    .noticeChange(notice.getNoticeChange())
                    .build();
        }
    }
 

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "공지사항 리스트 응답 DTO")
    public static class ResponseListItem {
    	
        @Schema(description = "공지사항번호", example = "1")
        private Long noticeId;
 
        @Schema(description = "제목", example = "점검 공지")
        private String noticeTitle;
 
        @Schema(description = "유형", example = "CHECK")
        private NoticeKind noticeKind;
 
        @Schema(description = "작성일", example = "2026-04-23T10:00:00")
        private LocalDateTime noticeDate;
 
        @Schema(description = "수정일", example = "2026-04-25T10:00:00")
        private LocalDateTime noticeChange;
 
        // Entity -> Response DTO 변환
        public static ResponseListItem from(Notice notice) {
            return ResponseListItem.builder()
                    .noticeId(notice.getNoticeId())
                    .noticeTitle(notice.getNoticeTitle())
                    .noticeKind(notice.getNoticeKind())
                    .noticeDate(notice.getNoticeDate())
                    .noticeChange(notice.getNoticeChange())
                    .build();
        }
    }

}