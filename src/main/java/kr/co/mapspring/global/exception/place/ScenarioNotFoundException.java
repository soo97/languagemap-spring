package kr.co.mapspring.global.exception.place;

public class ScenarioNotFoundException extends RuntimeException {
	
	public ScenarioNotFoundException() {
		super("존재하지 않는 시나리오입니다.");
	}

}
