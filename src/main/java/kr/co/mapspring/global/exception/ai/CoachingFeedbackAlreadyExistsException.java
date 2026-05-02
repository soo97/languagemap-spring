package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingFeedbackAlreadyExistsException extends CustomException {

    public CoachingFeedbackAlreadyExistsException() {
        super(ErrorCode.COACHING_FEEDBACK_ALREADY_EXISTS);
    }
}