package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class MissionNotFoundException extends CustomException {
	public MissionNotFoundException() {
		super(ErrorCode.MISSION_NOT_FOUND);
	}
}
