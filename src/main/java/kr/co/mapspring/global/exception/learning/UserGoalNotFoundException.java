package kr.co.mapspring.global.exception.learning;

public class UserGoalNotFoundException extends RuntimeException {

    public UserGoalNotFoundException() { super("존재하지 않는 사용자 목표입니다."); }

}
