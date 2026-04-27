package kr.co.mapspring.learning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.mapspring.learning.entity.GoalMaster;
import kr.co.mapspring.learning.entity.UserGoal;
import kr.co.mapspring.learning.enums.GoalPeriodType;
import kr.co.mapspring.learning.enums.GoalType;
import kr.co.mapspring.learning.enums.UserGoalStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class LearningGoalDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "학습 목표 선택 요청 DTO")
    public static class RequestSelectGoal {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "목표 마스터 ID", example = "1")
        private Long goalMasterId;
    }

    @Getter
    @Builder
    @Schema(description = "사용자 학습 목표 응답 DTO")
    public static class ResponseUserGoal {

        @Schema(description = "사용자 목표 ID", example = "1")
        private Long userGoalId;

        @Schema(description = "목표 마스터 ID", example = "1")
        private Long goalMasterId;

        @Schema(description = "목표 이름", example = "하루 1회 학습")
        private String goalTitle;

        @Schema(description = "목표 타입", example = "STUDY_COUNT")
        private GoalType goalType;

        @Schema(description = "목표 기간 타입", example = "DAILY")
        private GoalPeriodType periodType;

        @Schema(description = "목표 값", example = "1")
        private Integer targetValue;

        @Schema(description = "현재 진행 값", example = "0")
        private Integer currentValue;

        @Schema(description = "목표 상태", example = "ACTIVE")
        private UserGoalStatus status;

        @Schema(description = "시작 날짜", example = "2026-04-25")
        private LocalDate startDate;

        @Schema(description = "종료 날짜", example = "2026-04-26")
        private LocalDate endDate;

        public static ResponseUserGoal from(UserGoal userGoal) {
            GoalMaster goalMaster = userGoal.getGoalMaster();

            return ResponseUserGoal.builder()
                    .userGoalId(userGoal.getUserGoalId())
                    .goalMasterId(goalMaster.getGoalMasterId())
                    .goalTitle(goalMaster.getGoalTitle())
                    .goalType(goalMaster.getGoalType())
                    .periodType(goalMaster.getPeriodType())
                    .targetValue(goalMaster.getTargetValue())
                    .currentValue(userGoal.getCurrentValue())
                    .status(userGoal.getStatus())
                    .startDate(userGoal.getStartDate())
                    .endDate(userGoal.getEndDate())
                    .build();
        }
    }

    @Getter
    @Builder
    @Schema(description = "목표 마스터 응답 DTO")
    public static class ResponseGoalMaster {

        @Schema(description = "목표 마스터 ID", example = "1")
        private Long goalMasterId;

        @Schema(description = "목표 이름", example = "하루 1회 학습")
        private String goalTitle;

        @Schema(description = "목표 타입", example = "STUDY_COUNT")
        private GoalType goalType;

        @Schema(description = "목표 기간 타입", example = "DAILY")
        private GoalPeriodType periodType;

        @Schema(description = "목표 값", example = "1")
        private Integer targetValue;

        public static ResponseGoalMaster from(GoalMaster goalMaster) {
            return ResponseGoalMaster.builder()
                    .goalMasterId(goalMaster.getGoalMasterId())
                    .goalTitle(goalMaster.getGoalTitle())
                    .goalType(goalMaster.getGoalType())
                    .periodType(goalMaster.getPeriodType())
                    .targetValue(goalMaster.getTargetValue())
                    .build();
        }
    }
}
