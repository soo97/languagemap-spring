package kr.co.mapspring.global.exception.favorite;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FavoritePlaceNotFoundException extends CustomException {

    public FavoritePlaceNotFoundException() { super(ErrorCode.FAVORITE_PLACE_NOT_FOUND); }

}
