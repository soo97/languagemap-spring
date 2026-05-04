package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class MissionSessionNotFoundException extends CustomException {
	public MissionSessionNotFoundException() {
		super(ErrorCode.MISSION_SESSION_NOT_FOUND);
	}

}
