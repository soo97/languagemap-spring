package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class InvalidReportReasonException extends CustomException {
    public InvalidReportReasonException() { super(ErrorCode.BAD_REQUEST, "신고 사유는 필수입니다."); }
}
