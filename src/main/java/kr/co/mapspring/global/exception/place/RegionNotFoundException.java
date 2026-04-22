package kr.co.mapspring.global.exception.place;

public class RegionNotFoundException extends RuntimeException{

	public RegionNotFoundException() {
		super("존재하지 않는 지역입니다.");
	}
}
