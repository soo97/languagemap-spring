package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingFeedbackNotFoundException extends CustomException {

    public CoachingFeedbackNotFoundException() {
        super(ErrorCode.COACHING_FEEDBACK_NOT_FOUND);
    }
}