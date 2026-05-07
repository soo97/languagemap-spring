package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingMessageNotFoundException extends CustomException {

    public CoachingMessageNotFoundException() {
        super(ErrorCode.COACHING_MESSAGE_NOT_FOUND);
    }
}