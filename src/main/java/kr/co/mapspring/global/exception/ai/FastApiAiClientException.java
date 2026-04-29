package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FastApiAiClientException extends CustomException {

    public FastApiAiClientException() {
        super(ErrorCode.FASTAPI_CLIENT_ERROR);
    }
}