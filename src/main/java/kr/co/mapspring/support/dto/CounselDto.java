package kr.co.mapspring.support.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.support.entity.CounselKind;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class CounselDto {


    @Getter
    @Setter
    @Schema(description = "문의 작성 요청 DTO (일반 사용자)")
    public static class RequestCreate {
        @Schema(description = "문의 제목", example = "로그인 후 홈 이동 여부")
        private String counselName;

        @Schema(description = "문의 유형", example = "LOGIN_SIGNUP")
        private CounselKind counselKind;

        @Schema(description = "문의 내용", example = "로그인 완료 후 홈으로 이동하는 플로우를 확인하고 싶어요.")
        private String counselText;
    }




    @Getter
    @Setter
    @Schema(description = "문의 목록 아이템 응답 DTO")
    public static class ResponseListItem {
        @Schema(description = "문의 번호", example = "1")
        private Long counselId;

        @Schema(description = "문의 제목", example = "로그인 후 홈 이동 여부")
        private String counselName;

        @Schema(description = "문의 유형", example = "LOGIN_SIGNUP")
        private CounselKind counselKind;

        @Schema(description = "작성일", example = "2026-04-23T10:00:00")
        private LocalDateTime counselDate;

        @Schema(description = "답변 여부 (답변 있으면 true)", example = "true")
        private boolean hasAnswer;
    }


    @Getter
    @Setter
    @Schema(description = "문의 상세 응답 DTO")
    public static class ResponseDetail {
        @Schema(description = "문의 번호", example = "1")
        private Long counselId;

        @Schema(description = "작성자 이름", example = "홍길동")
        private String userName;

        @Schema(description = "문의 제목", example = "로그인 후 홈 이동 여부")
        private String counselName;

        @Schema(description = "문의 유형", example = "LOGIN_SIGNUP")
        private CounselKind counselKind;

        @Schema(description = "문의 내용", example = "로그인 완료 후 홈으로 이동하는 플로우를 확인하고 싶어요.")
        private String counselText;

        @Schema(description = "작성일", example = "2026-04-23T10:00:00")
        private LocalDateTime counselDate;

        @Schema(description = "이미지 목록")
        private List<String> images;

        @Schema(description = "답변 목록")
        private List<ResponseAnswer> answers;
    }

    @Getter
    @Setter
    @Schema(description = "답변 응답 DTO")
    public static class ResponseAnswer {
        @Schema(description = "답변 번호", example = "1")
        private Long answerId;

        @Schema(description = "답변 작성자 이름", example = "Mapingo 운영팀")
        private String userName;

        @Schema(description = "답변 내용", example = "안녕하세요. 로그인 완료 후 홈 화면으로 이동합니다.")
        private String answer;
    }
}