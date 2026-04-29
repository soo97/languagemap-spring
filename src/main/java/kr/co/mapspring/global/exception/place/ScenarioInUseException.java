package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class ScenarioInUseException extends CustomException {
	
	public ScenarioInUseException() {
		super(ErrorCode.SCENARIO_IN_USE);
	}

}
