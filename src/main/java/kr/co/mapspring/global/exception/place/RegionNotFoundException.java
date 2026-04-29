package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class RegionNotFoundException extends CustomException {

	public RegionNotFoundException() {
		super(ErrorCode.REGION_NOT_FOUND);
	}
}
