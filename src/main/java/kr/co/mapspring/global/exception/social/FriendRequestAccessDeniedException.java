package kr.co.mapspring.global.exception.social;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class FriendRequestAccessDeniedException extends CustomException {
    public FriendRequestAccessDeniedException() { super(ErrorCode.FRIEND_REQUEST_ACCESS_DENIED); }
}
