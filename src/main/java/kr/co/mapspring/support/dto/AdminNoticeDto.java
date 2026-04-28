package kr.co.mapspring.support.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.mapspring.support.entity.NoticeKind;
import lombok.Getter;
import lombok.Setter;

public class AdminNoticeDto {

    @Getter
    @Setter
    @Schema(description = "공지사항 작성 요청 DTO(관리자)")
    public static class RequestCreate {
    	@NotBlank
        @Schema(description = "제목", example = "점검 공지")
        private String noticeTitle;
    	@NotNull
        @Schema(description = "유형", example = "점검")
        private NoticeKind noticeKind;
    	@NotBlank
        @Schema(description = "공지사항 내용", example = "8월 19일날 점검을 하게 되어 접속이 안될 수 있습니다.")
        private String noticeText;
        @Schema(description = "이미지", example = "dog.jpg")
        private List<String> noticeImg;
        @Schema(description = "링크", example = "https://www.naver.com/")
        private List<String> noticeUrl;
    }


    @Getter
    @Setter
    @Schema(description = "공지사항 수정 요청 DTO(관리자)")
    public static class RequestUpdate {
    	@NotBlank
    	@Schema(description = "제목", example = "점검 공지")
        private String noticeTitle;
    	@NotNull
    	@Schema(description = "유형", example = "점검")
        private NoticeKind noticeKind;
    	@NotBlank
    	@Schema(description = "공지사항 내용", example = "8월 19일날 점검을 하게 되어 접속이 안될 수 있습니다.")
        private String noticeText;
    	@Schema(description = "이미지", example = "dog.jpg")
        private List<String> noticeImg;
    	@Schema(description = "링크", example = "https://www.naver.com/")
        private List<String> noticeUrl;
    }
    
    @Getter
    @Setter
    @Schema(description = "공지사항 단건 상세 응답 DTO")
    public static class ResponseDetail {
    	@Schema(description = "공지사항번호", example = "1")
        private Long noticeId;
    	@Schema(description = "제목", example = "점검 공지")
        private String noticeTitle;
    	@Schema(description = "유형", example = "점검")
        private NoticeKind noticeKind;
    	@Schema(description = "공지사항 내용", example = "8월 19일날 점검을 하게 되어 접속이 안될 수 있습니다.")
        private String noticeText;
    	@Schema(description = "이미지", example = "dog.jpg")
        private List<String> noticeImg;
    	@Schema(description = "링크", example = "https://www.naver.com/")
        private List<String> noticeUrl;
    	@Schema(description = "작성일", example = "2026.04.23")
        private LocalDateTime noticeDate;
    	@Schema(description = "수정일", example = "2026.04.25")
        private LocalDateTime noticeChange;
    }


    @Getter
    @Setter
    @Schema(description = "공지사항 리스트 응답 DTO")
    public static class ResponseListItem {
    	@Schema(description = "공지사항번호", example = "1")
        private Long noticeId;
        @Schema(description = "제목", example = "점검 공지")
        private String noticeTitle;
        @Schema(description = "유형", example = "점검")
        private NoticeKind noticeKind;
    	@Schema(description = "작성일", example = "2026.04.23")
        private LocalDateTime noticeDate;
    	@Schema(description = "수정일", example = "2026.04.25")
        private LocalDateTime noticeChange;
    }
}
