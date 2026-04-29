package kr.co.mapspring.global.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.co.mapspring.user.entity.User;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration

    ) {
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;

    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(user.getUserId()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .claim("tokenType", "ACCESS")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }
    
    	// Refresh Token 생성
    public String createRefreshToken(User user) {
        Date now = new Date();

        // 현재 시간 + Refresh Token 만료 시간
        Date expiration = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                // 어떤 사용자의 Refresh Token인지 식별하기 위해 userId를 subject에 저장
                .subject(String.valueOf(user.getUserId()))

                // Access Token과 구분하기 위한 타입 값
                .claim("tokenType", "REFRESH")

                // 발급 시간
                .issuedAt(now)

                // 만료 시간
                .expiration(expiration)

                // secret key로 서명
                .signWith(getSecretKey())

                // JWT 문자열 생성
                .compact();
    }
    
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = getClaims(token);

            // 나중에 Refresh Token과 구분하기 위해 tokenType을 확인한다.
            return "ACCESS".equals(claims.get("tokenType", String.class));

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
 // Refresh Token 유효성 검증
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaims(token);

            // tokenType이 REFRESH인 토큰만 Refresh Token으로 인정한다.
            return "REFRESH".equals(claims.get("tokenType", String.class));

        } catch (JwtException | IllegalArgumentException e) {
            // 만료, 서명 오류, 토큰 형식 오류 등은 false 처리한다.
            return false;
        }
    }
    
    

    // JWT subject에서 userId를 꺼낸다.
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    // JWT 내부 Claims 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 문자열 secret을 JWT 서명용 SecretKey로 변환
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}