package kr.co.mapspring.global.exception.user;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class PasswordConfirmMismatchException extends CustomException {

    public PasswordConfirmMismatchException() {
        super(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
    }
}