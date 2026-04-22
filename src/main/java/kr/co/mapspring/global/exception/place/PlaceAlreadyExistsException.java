package kr.co.mapspring.global.exception.place;

public class PlaceAlreadyExistsException extends RuntimeException {
	
	public PlaceAlreadyExistsException() {
		super("이미 존재하는 지역입니다.");
	}

}
