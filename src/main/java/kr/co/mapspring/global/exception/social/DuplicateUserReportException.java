package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class DuplicateUserReportException extends CustomException {
    public DuplicateUserReportException() {
        super(ErrorCode.DUPLICATE_USER_REPORT);
    }
}
