package kr.co.mapspring.global.exception.user;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class PrivacyTermsRequiredException extends CustomException {

    public PrivacyTermsRequiredException() {
        super(ErrorCode.PRIVACY_TERMS_REQUIRED);
    }
}