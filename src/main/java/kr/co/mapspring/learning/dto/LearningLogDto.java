package kr.co.mapspring.learning.dto;

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
    public static class ResponseLog {

        private Long studyLogId;
        private StudyType studyType;
        private Integer earnedExp;

        private Integer naturalnessScore;
        private Integer fluencyScore;
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
