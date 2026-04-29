package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class InvalidReportUserException extends CustomException {
    public InvalidReportUserException() {
        super(ErrorCode.BAD_REQUEST, "사용자 ID는 필수입니다.");
    }
}
