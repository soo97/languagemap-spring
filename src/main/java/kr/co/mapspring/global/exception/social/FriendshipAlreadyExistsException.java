package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FriendshipAlreadyExistsException extends CustomException {
    public FriendshipAlreadyExistsException() { super(ErrorCode.FRIENDSHIP_ALREADY_EXISTS); }
}
