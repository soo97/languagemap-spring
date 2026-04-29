package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class InvalidSocialUserException extends CustomException {
    public InvalidSocialUserException() { super(ErrorCode.BAD_REQUEST, "userId는 필수입니다."); }
}
