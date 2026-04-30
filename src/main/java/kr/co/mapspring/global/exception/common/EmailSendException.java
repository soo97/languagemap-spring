package kr.co.mapspring.global.exception.common;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class EmailSendException extends CustomException {
    public EmailSendException() {
        super(ErrorCode.EMAIL_SEND_FAILED);
    }
}
 