package kr.co.mapspring.global.exception.place;

public class MissionNotFoundException extends RuntimeException {
	public MissionNotFoundException() {
		super("존재하지 않는 미션입니다.");
	}
}
