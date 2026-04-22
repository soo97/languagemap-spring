package kr.co.mapspring.global.exception.user;

public class InactiveUserException extends RuntimeException {

    public InactiveUserException() {
        super("비활성 사용자입니다.");
    }
}