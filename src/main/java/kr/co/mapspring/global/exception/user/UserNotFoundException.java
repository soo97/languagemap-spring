package kr.co.mapspring.global.exception.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("존재하지 않는 이메일입니다.");
    }
}