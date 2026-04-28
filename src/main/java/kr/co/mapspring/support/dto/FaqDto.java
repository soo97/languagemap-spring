package kr.co.mapspring.support.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class FaqDto {

    @Getter
    @Setter
    @Schema(description = "FAQ 목록 및 단건 응답 DTO")
    public static class Response {
        @Schema(description = "FAQ 번호", example = "1")
        private Long faqId;

        @Schema(description = "질문", example = "로그인 후 홈으로 돌아가나요?")
        private String question;

        @Schema(description = "답변", example = "네. 시연용 플로우에서는 로그인 완료 후 홈 화면으로 이동해요.")
        private String answer;
    }
}
