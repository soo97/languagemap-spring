package kr.co.mapspring.global.exception.chat;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class InvalidChatMessageException extends CustomException {

    public InvalidChatMessageException() {
        super(ErrorCode.INVALID_CHAT_MESSAGE);
    }
}
