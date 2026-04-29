package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class LearningSessionNotFoundException extends CustomException {

    public LearningSessionNotFoundException() {
        super(ErrorCode.LEARNING_SESSION_NOT_FOUND);
    }
}