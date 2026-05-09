package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class AiCoachingAccessDeniedException extends CustomException {

    public AiCoachingAccessDeniedException() {
        super(ErrorCode.AI_COACHING_ACCESS_DENIED);
    }
}