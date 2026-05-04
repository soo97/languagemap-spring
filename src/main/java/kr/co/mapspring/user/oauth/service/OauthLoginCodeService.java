package kr.co.mapspring.user.oauth.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.user.oauth.dto.OauthLoginDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthLoginCodeService {

    private static final String OAUTH_LOGIN_CODE_PREFIX = "oauth:login:";
    private static final Duration OAUTH_LOGIN_CODE_TTL = Duration.ofMinutes(3);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    /*
     * OAuth 로그인 성공 후 accessToken/refreshToken을 직접 URL에 싣지 않기 위해
     * Redis에 1회용 code 기준으로 임시 저장합니다.
     */
    public String saveToken(
            String accessToken,
            String refreshToken,
            boolean profileRequired
    ) {
        String code = UUID.randomUUID().toString();
        String key = OAUTH_LOGIN_CODE_PREFIX + code;

        OauthLoginDto.ResponseToken response = OauthLoginDto.ResponseToken.of(
                accessToken,
                refreshToken,
                profileRequired
        );

        try {
            String value = objectMapper.writeValueAsString(response);

            stringRedisTemplate.opsForValue()
                    .set(key, value, OAUTH_LOGIN_CODE_TTL);

            return code;
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * 프론트가 전달한 code로 임시 저장된 토큰을 조회합니다.
     * 조회 후 즉시 삭제하여 1회용으로만 사용되게 합니다.
     */
    @Transactional
    public OauthLoginDto.ResponseToken consumeToken(String code) {
        String key = OAUTH_LOGIN_CODE_PREFIX + code;
        String value = stringRedisTemplate.opsForValue().get(key);

        if (value == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "유효하지 않거나 만료된 OAuth 로그인 코드입니다.");
        }

        stringRedisTemplate.delete(key);

        try {
            return objectMapper.readValue(value, OauthLoginDto.ResponseToken.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}