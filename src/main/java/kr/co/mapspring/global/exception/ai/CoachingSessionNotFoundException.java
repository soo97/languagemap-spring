package kr.co.mapspring.global.exception.ai;

public class CoachingSessionNotFoundException extends RuntimeException {

    public CoachingSessionNotFoundException() {
        super("코칭 세션을 찾을 수 없습니다.");
    }
}