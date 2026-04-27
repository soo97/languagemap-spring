package kr.co.mapspring.global.exception.user;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class TermsNotFoundException extends CustomException {

    public TermsNotFoundException() {
        super(ErrorCode.TERMS_NOT_FOUND);
    }
}