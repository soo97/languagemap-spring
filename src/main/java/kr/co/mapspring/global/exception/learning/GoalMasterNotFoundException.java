package kr.co.mapspring.global.exception.learning;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class GoalMasterNotFoundException extends CustomException {

    public GoalMasterNotFoundException() { super(ErrorCode.GOAL_MASTER_NOT_FOUND); }

}
