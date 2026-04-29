package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingSessionNotFoundException extends CustomException {

    public CoachingSessionNotFoundException() {
        super(ErrorCode.COACHING_SESSION_NOT_FOUND);
    }
}