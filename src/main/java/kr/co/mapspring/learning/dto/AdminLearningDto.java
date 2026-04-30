package kr.co.mapspring.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.StudyLog;
import kr.co.mapspring.learning.entity.StudyScore;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminLearningDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자 학습 목표 생성 요청 DTO")
    public static class RequestCreateGoal {

        @Schema(description = "배지 ID", example = "1")
        private Long badgeId;

        @Schema(description = "목표 타입", example = "STUDY_COUNT")
        private GoalType goalType;

        @Schema(description = "목표 이름", example = "하루 학습 3회")
        private String goalTitle;

        @Schema(description = "목표 설명", example = "하루에 학습을 3회 완료합니다.")
        private String goalDescription;

        @Schema(description = "목표 값", example = "3")
        private Integer targetValue;

        @Schema(description = "목표 기간 타입", example = "DAILY")
        private GoalPeriodType periodType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자 학습 목표 수정 요청 DTO")
    public static class RequestUpdateGoal {

        @Schema(description = "배지 ID", example = "1")
        private Long badgeId;

        @Schema(description = "목표 타입", example = "STUDY_TIME")
        private GoalType goalType;

        @Schema(description = "목표 이름", example = "하루 학습 30분")
        private String goalTitle;

        @Schema(description = "목표 설명", example = "하루에 30분 이상 학습합니다.")
        private String goalDescription;

        @Schema(description = "목표 값", example = "30")
        private Integer targetValue;

        @Schema(description = "목표 기간 타입", example = "DAILY")
        private GoalPeriodType periodType;
    }

    @Getter
    @Builder
    @Schema(description = "관리자 학습 기록 응답 DTO")
    public static class ResponseStudyLog {

        @Schema(description = "학습 기록 ID", example = "1")
        private Long studyLogId;

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "학습 세션 ID", example = "1")
        private Long sessionId;

        @Schema(description = "학습 타입", example = "SCENARIO")
        private StudyType studyType;

        @Schema(description = "획득 경험치", example = "20")
        private Integer earnedExp;

        @Schema(description = "자연스러움 점수", example = "80")
        private Integer naturalnessScore;

        @Schema(description = "유창성 점수", example = "70")
        private Integer fluencyScore;

        @Schema(description = "종합 점수", example = "75")
        private Integer totalScore;

        public static ResponseStudyLog from(StudyLog studyLog, StudyScore studyScore) {
            return ResponseStudyLog.builder()
                    .studyLogId(studyLog.getStudyLogId())
                    .userId(studyLog.getUser().getUserId())
                    .sessionId(studyLog.getLearningSession().getSessionId())
                    .studyType(studyLog.getStudyType())
                    .earnedExp(studyLog.getEarnedExp())
                    .naturalnessScore(studyScore != null ? studyScore.getNaturalnessScore() : null)
                    .fluencyScore(studyScore != null ? studyScore.getFluencyScore() : null)
                    .totalScore(studyScore != null ? studyScore.getTotalScore() : null)
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "관리자 목표 마스터 응답 DTO")
    public static class ResponseGoalMaster {

        @Schema(description = "목표 마스터 ID", example = "1")
        private Long goalMasterId;

        @Schema(description = "목표 이름", example = "하루 1회 학습")
        private String goalTitle;

        @Schema(description = "목표 설명", example = "하루에 학습을 1회 완료합니다.")
        private String goalDescription;

        @Schema(description = "목표 타입", example = "STUDY_COUNT")
        private GoalType goalType;

        @Schema(description = "목표 기간 타입", example = "DAILY")
        private GoalPeriodType periodType;

        @Schema(description = "목표 값", example = "1")
        private Integer targetValue;

        @Schema(description = "활성화 여부", example = "true")
        private boolean active;

        public static ResponseGoalMaster from(GoalMaster goalMaster) {
            return ResponseGoalMaster.builder()
                    .goalMasterId(goalMaster.getGoalMasterId())
                    .goalTitle(goalMaster.getGoalTitle())
                    .goalDescription(goalMaster.getGoalDescription())
                    .goalType(goalMaster.getGoalType())
                    .periodType(goalMaster.getPeriodType())
                    .targetValue(goalMaster.getTargetValue())
                    .active(goalMaster.isActive())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자 목표 활성화 상태 변경 요청 DTO")
    public static class RequestUpdateGoalActive {

        @Schema(description = "활성화 여부", example = "false")
        private boolean active;
    }

}
