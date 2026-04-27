package kr.co.mapspring.global.exception.favorite;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FavoritePlaceAlreadyExistsException extends CustomException {

    public FavoritePlaceAlreadyExistsException() { super(ErrorCode.FAVORITE_PLACE_ALREADY_EXISTS); }

}
