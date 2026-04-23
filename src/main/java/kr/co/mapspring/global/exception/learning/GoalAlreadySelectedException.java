package kr.co.mapspring.global.exception.learning;

public class GoalAlreadySelectedException extends RuntimeException {

    public GoalAlreadySelectedException() {super("이미 선택한 학습 목표입니다.");}

}
