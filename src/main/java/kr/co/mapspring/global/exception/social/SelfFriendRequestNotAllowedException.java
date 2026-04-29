package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class SelfFriendRequestNotAllowedException extends CustomException {

    public SelfFriendRequestNotAllowedException() { super(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED); }
}
