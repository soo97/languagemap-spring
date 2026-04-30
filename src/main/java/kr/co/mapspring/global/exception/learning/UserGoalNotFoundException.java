package kr.co.mapspring.global.exception.learning;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class UserGoalNotFoundException extends CustomException {

    public UserGoalNotFoundException() { super(ErrorCode.USER_GOAL_NOT_FOUND); }

}
