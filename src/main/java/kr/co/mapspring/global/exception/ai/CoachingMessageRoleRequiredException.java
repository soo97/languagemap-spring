package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingMessageRoleRequiredException extends CustomException {

    public CoachingMessageRoleRequiredException() {
        super(ErrorCode.COACHING_MESSAGE_ROLE_REQUIRED);
    }
}