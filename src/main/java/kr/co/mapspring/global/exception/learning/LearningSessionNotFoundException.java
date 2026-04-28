package kr.co.mapspring.global.exception.learning;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class LearningSessionNotFoundException extends CustomException {
    public LearningSessionNotFoundException() { super(ErrorCode.NOT_FOUND, "학습 세션을 찾을 수 없습니다."); }
}
