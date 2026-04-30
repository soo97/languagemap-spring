package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class PlaceNotFoundException extends CustomException {
	
	public PlaceNotFoundException() {
		super(ErrorCode.PLACE_NOT_FOUND);
	}
}
