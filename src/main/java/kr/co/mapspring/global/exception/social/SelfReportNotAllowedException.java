package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class SelfReportNotAllowedException extends CustomException {
    public SelfReportNotAllowedException() { super(ErrorCode.BAD_REQUEST, "자기 자신을 신고할 수 없습니다."); }
}
