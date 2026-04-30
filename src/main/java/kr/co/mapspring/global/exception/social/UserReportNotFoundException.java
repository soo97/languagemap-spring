package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class UserReportNotFoundException extends CustomException {
    public UserReportNotFoundException() {
        super(ErrorCode.NOT_FOUND, "신고 내역을 찾을 수 없습니다.");
    }
}
