package kr.co.mapspring.global.exception.ai;

public class CoachingMessageRoleRequiredException extends RuntimeException {

    public CoachingMessageRoleRequiredException() {
        super("메시지 역할은 필수입니다.");
    }
}