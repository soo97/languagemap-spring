package kr.co.mapspring.learning.dto;

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
    public static class RequestSelectGoal {
        private Long userId;
        private Long goalMasterId;
    }

    @Getter
    @Builder
    public static class ResponseUserGoal {
        private Long userGoalId;
        private Long goalMasterId;
        private String goalTitle;
        private GoalType goalType;
        private GoalPeriodType periodType;
        private Integer targetValue;
        private Integer currentValue;
        private UserGoalStatus status;
        private LocalDate startDate;
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
    public static class ResponseGoalMaster {
        private Long goalMasterId;
        private String goalTitle;
        private GoalType goalType;
        private GoalPeriodType periodType;
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
