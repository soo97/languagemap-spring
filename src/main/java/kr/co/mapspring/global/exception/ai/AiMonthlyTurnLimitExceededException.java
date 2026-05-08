package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class AiMonthlyTurnLimitExceededException extends CustomException {

    public AiMonthlyTurnLimitExceededException() {
        super(ErrorCode.AI_MONTHLY_TURN_LIMIT_EXCEEDED);
    }
}