package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class AssistantMessageRequiredException extends CustomException {

    public AssistantMessageRequiredException() {
        super(ErrorCode.ASSISTANT_MESSAGE_REQUIRED);
    }
}