package kr.co.mapspring.global.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AiUsageLimitPolicy {

    public static final int MONTHLY_COACHING_SESSION_LIMIT = 15;
    public static final int MAX_TURN_PER_SESSION = 5;
    public static final int MONTHLY_TOTAL_TURN_LIMIT = 75;
    public static final int YOUTUBE_MAX_RESULTS = 3;

    // 발표/시연용 테스트 계정: 유료 여부/사용량 제한 모두 예외
    private static final List<Long> TEST_USER_IDS = List.of(1L);

    // 유료 계정: AI Coaching 사용 가능 기간
    private static final Map<Long, LocalDate> VIP_USER_EXPIRES_AT = Map.of();

    private AiUsageLimitPolicy() {}

    public static boolean isTestUser(Long userId) {
        return userId != null && TEST_USER_IDS.contains(userId);
    }

    public static boolean isVipUser(Long userId) {
        return userId != null && VIP_USER_EXPIRES_AT.containsKey(userId);
    }

    public static boolean hasValidAiCoachingAccess(Long userId) {
        if (isTestUser(userId)) {
            return true;
        }

        LocalDate expiresAt = VIP_USER_EXPIRES_AT.get(userId);

        return expiresAt != null && !LocalDate.now().isAfter(expiresAt);
    }

    public static boolean shouldSkipUsageLimit(Long userId) {
        return isTestUser(userId);
    }
}