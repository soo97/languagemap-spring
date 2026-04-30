package kr.co.mapspring.user.oauth.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.co.mapspring.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OauthUserDto implements OAuth2User {

    /*
     * 우리 서비스의 User 엔티티입니다.
     * OAuth 로그인 성공 후 JWT 발급에 사용합니다.
     */
    private final User user;

    /*
     * Google에서 받은 원본 사용자 정보입니다.
     * sub, email, name, picture, email_verified 등이 들어옵니다.
     */
    private final Map<String, Object> attributes;

    /*
     * Spring Security 권한 정보입니다.
     * ROLE_USER, ROLE_ADMIN 형식으로 저장합니다.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /*
     * Spring Security에서 OAuth 사용자 식별자로 사용하는 값입니다.
     * 우리 서비스 기준에서는 userId를 사용합니다.
     */
    @Override
    public String getName() {
        return String.valueOf(user.getUserId());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}