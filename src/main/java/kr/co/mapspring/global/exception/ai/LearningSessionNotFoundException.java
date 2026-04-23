package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.ResourceNotFoundException;

public class LearningSessionNotFoundException extends ResourceNotFoundException {

    public LearningSessionNotFoundException() {
        super("학습 세션을 찾을 수 없습니다.");
    }
}