package kr.co.mapspring.global.exception.favorite;

public class FavoritePlaceNotFoundException extends RuntimeException {

    public FavoritePlaceNotFoundException() { super("존재하지 않는 즐겨찾기 장소입니다."); }

}
