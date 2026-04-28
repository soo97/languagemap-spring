package kr.co.mapspring.global.jwt;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    // Redis key prefix
    // 예: refresh:1
    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    private final StringRedisTemplate stringRedisTemplate;

    private final long refreshTokenExpiration;

    public RefreshTokenService(
            StringRedisTemplate stringRedisTemplate,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // Refresh Token을 Redis에 저장한다.
    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;

        // Redis에 refresh:{userId} = refreshToken 형태로 저장한다.
        // Duration을 함께 넘기면 해당 시간이 지난 뒤 자동 삭제된다.
        stringRedisTemplate.opsForValue()
                .set(key, refreshToken, Duration.ofMillis(refreshTokenExpiration));
    }
    
 // Redis에 저장된 Refresh Token을 조회한다.
    public String getRefreshToken(Long userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;

        return stringRedisTemplate.opsForValue().get(key);
    }

    // 요청으로 들어온 Refresh Token과 Redis에 저장된 Refresh Token이 같은지 확인한다.
    public boolean isRefreshTokenMatched(Long userId, String refreshToken) {
        String savedRefreshToken = getRefreshToken(userId);

        return refreshToken != null && refreshToken.equals(savedRefreshToken);
    }
    
    
}