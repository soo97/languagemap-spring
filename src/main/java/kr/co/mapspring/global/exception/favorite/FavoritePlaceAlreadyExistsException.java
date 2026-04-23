package kr.co.mapspring.global.exception.favorite;

public class FavoritePlaceAlreadyExistsException extends RuntimeException {

    public FavoritePlaceAlreadyExistsException() { super("이미 즐겨찾기한 장소입니다."); }
}
