package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class UserNotFoundForSocialException extends CustomException {
    public UserNotFoundForSocialException() { super(ErrorCode.USER_NOT_FOUND_FOR_SOCIAL); }
}
