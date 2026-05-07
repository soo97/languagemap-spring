package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingPronunciationResultNotFoundException extends CustomException {

    public CoachingPronunciationResultNotFoundException() {
        super(ErrorCode.COACHING_PRONUNCIATION_RESULT_NOT_FOUND);
    }
}