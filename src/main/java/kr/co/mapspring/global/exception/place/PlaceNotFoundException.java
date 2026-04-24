package kr.co.mapspring.global.exception.place;

public class PlaceNotFoundException extends RuntimeException {
	
	public PlaceNotFoundException() {
		super("존재하지 않는 장소입니다.");
	}
}
