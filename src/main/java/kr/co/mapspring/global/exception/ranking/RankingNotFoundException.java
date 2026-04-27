package kr.co.mapspring.global.exception.ranking;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class RankingNotFoundException extends CustomException {

    public RankingNotFoundException() { super(ErrorCode.RANKING_NOT_FOUND); }
}
