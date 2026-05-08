package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class AiSessionTurnLimitExceededException extends CustomException {

    public AiSessionTurnLimitExceededException() {
        super(ErrorCode.AI_SESSION_TURN_LIMIT_EXCEEDED);
    }
}