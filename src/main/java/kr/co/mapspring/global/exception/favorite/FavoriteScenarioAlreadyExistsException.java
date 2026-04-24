package kr.co.mapspring.global.exception.favorite;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FavoriteScenarioAlreadyExistsException extends CustomException {

    public FavoriteScenarioAlreadyExistsException() {
        super(ErrorCode.FAVORITE_SCENARIO_ALREADY_EXISTS);
    }

}
