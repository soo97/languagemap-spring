package kr.co.mapspring.global.exception.ai;

public class LearningSessionNotFoundException extends RuntimeException {

    public LearningSessionNotFoundException() {
        super("학습 세션을 찾을 수 없습니다.");
    }
}