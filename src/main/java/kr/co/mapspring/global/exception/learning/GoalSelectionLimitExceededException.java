package kr.co.mapspring.global.exception.learning;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class GoalSelectionLimitExceededException extends CustomException {

    public GoalSelectionLimitExceededException() { super(ErrorCode.GOAL_SELECTION_LIMIT_EXCEEDED); }

}
