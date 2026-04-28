package kr.co.mapspring.support.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class AdminFaqDto {

    @Getter
    @Setter
    @Schema(description = "FAQ 작성 요청 DTO (관리자)")
    public static class RequestCreate {
        @Schema(description = "질문", example = "로그인 후 홈으로 돌아가나요?")
        private String question;

        @Schema(description = "답변", example = "네. 시연용 플로우에서는 로그인 완료 후 홈 화면으로 이동해요.")
        private String answer;
    }

    @Getter
    @Setter
    @Schema(description = "FAQ 수정 요청 DTO (관리자)")
    public static class RequestUpdate {
        @Schema(description = "질문", example = "학습 기록은 어디서 확인하나요?")
        private String question;

        @Schema(description = "답변", example = "홈의 최근 학습 카드와 성장 리포트에서 확인할 수 있어요.")
        private String answer;
    }
}
