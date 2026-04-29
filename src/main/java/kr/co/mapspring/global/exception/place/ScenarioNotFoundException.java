package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class ScenarioNotFoundException extends CustomException {
	
	public ScenarioNotFoundException() {
		super(ErrorCode.SCENARIO_NOT_FOUND);
	}

}
