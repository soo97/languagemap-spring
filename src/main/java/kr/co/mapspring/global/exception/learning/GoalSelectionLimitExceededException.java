package kr.co.mapspring.global.exception.learning;

public class GoalSelectionLimitExceededException extends RuntimeException {

    public GoalSelectionLimitExceededException() { super("학습 목표는 최대 3개까지만 선택할 수 있습니다."); }

}
