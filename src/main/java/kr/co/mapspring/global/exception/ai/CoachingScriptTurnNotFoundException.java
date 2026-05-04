package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class CoachingScriptTurnNotFoundException extends CustomException {

    public CoachingScriptTurnNotFoundException() {
        super(ErrorCode.COACHING_SCRIPT_TURN_NOT_FOUND);
    }
}