package kr.co.mapspring.global.exception.place;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class ChatLimitExceededException extends CustomException {
	public ChatLimitExceededException() {
		super(ErrorCode.CHAT_LIMIT_EXCEED);
	}

}
