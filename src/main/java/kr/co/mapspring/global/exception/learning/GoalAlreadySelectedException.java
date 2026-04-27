package kr.co.mapspring.global.exception.learning;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class GoalAlreadySelectedException extends CustomException {

    public GoalAlreadySelectedException() {super(ErrorCode.GOAL_ALREADY_SELECTED);}

}
