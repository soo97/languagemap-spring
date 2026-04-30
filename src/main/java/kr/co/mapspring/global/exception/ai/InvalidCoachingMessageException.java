package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class InvalidCoachingMessageException extends CustomException {

    public InvalidCoachingMessageException() {
        super(ErrorCode.INVALID_COACHING_MESSAGE);
    }
}