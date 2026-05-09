package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class AiMonthlySessionLimitExceededException extends CustomException {

    public AiMonthlySessionLimitExceededException() {
        super(ErrorCode.AI_MONTHLY_SESSION_LIMIT_EXCEEDED);
    }
}