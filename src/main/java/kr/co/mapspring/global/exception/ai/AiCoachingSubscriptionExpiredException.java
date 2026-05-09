package kr.co.mapspring.global.exception.ai;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;

public class AiCoachingSubscriptionExpiredException extends CustomException {

    public AiCoachingSubscriptionExpiredException() {
        super(ErrorCode.AI_COACHING_SUBSCRIPTION_EXPIRED);
    }
}