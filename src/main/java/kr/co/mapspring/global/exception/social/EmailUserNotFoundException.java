package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class EmailUserNotFoundException extends CustomException {
    public EmailUserNotFoundException() { super(ErrorCode.NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다."); }
}
