package kr.co.mapspring.global.exception;

public class ResourceNotFoundException extends CustomException {

    public ResourceNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
