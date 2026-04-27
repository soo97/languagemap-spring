package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FriendshipNotFoundException extends CustomException {
    public FriendshipNotFoundException() { super(ErrorCode.FRIENDSHIP_NOT_FOUND); }
}
