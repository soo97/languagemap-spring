package kr.co.mapspring.global.exception.favorite;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FavoriteScenarioNotFoundException extends CustomException {

    public FavoriteScenarioNotFoundException() {
        super(ErrorCode.FAVORITE_SCENARIO_NOT_FOUND);
    }

}
