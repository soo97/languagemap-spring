package kr.co.mapspring.global.exception.user;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class InactiveUserException extends CustomException {

    public InactiveUserException() {
        super(ErrorCode.INACTIVE_USER);
    }
}