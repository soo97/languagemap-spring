package kr.co.mapspring.global.exception.user;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class ServiceTermsRequiredException extends CustomException {

    public ServiceTermsRequiredException() {
        super(ErrorCode.SERVICE_TERMS_REQUIRED);
    }
}