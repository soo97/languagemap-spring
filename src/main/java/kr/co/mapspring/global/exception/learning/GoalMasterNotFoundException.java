package kr.co.mapspring.global.exception.learning;

public class GoalMasterNotFoundException extends RuntimeException {

    public GoalMasterNotFoundException() {
        super("존재하지 않는 학습 목표입니다.");
    }

}
