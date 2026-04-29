package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class PythonAiClientException extends CustomException {

    public PythonAiClientException() {
        super(ErrorCode.PYTHON_AI_CLIENT_ERROR);
    }
}