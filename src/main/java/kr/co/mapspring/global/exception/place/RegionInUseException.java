package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class RegionInUseException extends CustomException {
	
	public RegionInUseException() {
		super(ErrorCode.REGION_IN_USE);
	}

}
