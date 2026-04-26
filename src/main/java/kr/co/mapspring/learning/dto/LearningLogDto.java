package kr.co.mapspring.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.StudyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LearningLogDto {

    @Getter
    @Builder
    @Schema(description = "학습 기록 응답 DTO")
    public static class ResponseLog {

        @Schema(description = "학습 기록 ID", example = "1")
        private Long studyLogId;

        @Schema(description = "세션 ID", example = "10")
        private Long sessionId;

        @Schema(description = "학습 타입", example = "SCENARIO")
        private StudyType studyType;

        @Schema(description = "획득 경험치", example = "20")
        private Integer earnedExp;

        @Schema(description = "자연스러움 점수", example = "80")
        private Integer naturalnessScore;

        @Schema(description = "유창성 점수", example = "70")
        private Integer fluencyScore;

        @Schema(description = "총 점수", example = "75")
        private Integer totalScore;

        public static ResponseLog from(StudyLog log, StudyScore score) {
            return ResponseLog.builder()
                    .studyLogId(log.getStudyLogId())
                    .studyType(log.getStudyType())
                    .earnedExp(log.getEarnedExp())
                    .naturalnessScore(score.getNaturalnessScore())
                    .fluencyScore(score.getFluencyScore())
                    .totalScore(score.getTotalScore())
                    .build();
        }
    }
}
