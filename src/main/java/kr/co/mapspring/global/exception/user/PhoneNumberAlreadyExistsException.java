package kr.co.mapspring.global.exception.user;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class PhoneNumberAlreadyExistsException extends CustomException {
    public PhoneNumberAlreadyExistsException() {
        super(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
    }
}