package kr.co.mapspring.user.oauth.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.oauth.dto.OauthUserDto;
import kr.co.mapspring.user.oauth.entity.OauthUser;
import kr.co.mapspring.user.oauth.enums.SocialProvider;
import kr.co.mapspring.user.oauth.repository.OauthUserRepository;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final OauthUserRepository oauthUserRepository;

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*
         * 1. Spring Security 기본 서비스가 Google에서 사용자 정보를 가져옵니다.
         */
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        /*
         * 2. registrationId를 확인합니다.
         * application.properties의 registration.google에서 google에 해당합니다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider provider = resolveProvider(registrationId);

        /*
         * 3. Google 사용자 정보를 추출합니다.
         *
         * Google 주요 attributes:
         * - sub: Google 사용자 고유 ID
         * - email: Google 이메일
         * - name: Google 이름
         * - email_verified: 이메일 검증 여부
         */
        Map<String, Object> attributes = oauth2User.getAttributes();

        String providerUserId = getRequiredAttribute(attributes, "sub");
        String email = getRequiredAttribute(attributes, "email");
        String name = resolveName(attributes.get("name"), email);

        validateEmailVerified(attributes);

        /*
         * 4. oauth_account 조회, 기존 User 연결, 또는 신규 소셜 User 생성을 처리합니다.
         */
        User user = findOrCreateUser(provider, providerUserId, email, name);

        /*
         * 5. 비활성 사용자는 로그인 차단합니다.
         */
        if (!user.isActive()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("inactive_user"),
                    "비활성화된 사용자입니다."
            );
        }

        /*
         * 6. 기존 JWT 필터와 동일하게 ROLE_ 접두사를 붙입니다.
         */
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        /*
         * 7. Spring Security 인증 객체에 들어갈 DTO를 반환합니다.
         */
        return new OauthUserDto(user, attributes, authorities);
    }

    private User findOrCreateUser(
            SocialProvider provider,
            String providerUserId,
            String email,
            String name
    ) {
        /*
         * 1순위:
         * 이미 oauth_account에 연결된 소셜 계정이면 해당 User로 로그인합니다.
         */
        return oauthUserRepository.findByProviderAndProviderUserId(provider, providerUserId)
                .map(oauthUser -> {
                    /*
                     * Google 이메일이 바뀌었을 가능성을 고려해 최신 이메일로 갱신합니다.
                     */
                    oauthUser.updateProviderEmail(email);
                    return oauthUser.getUser();
                })
                .orElseGet(() -> connectOrCreateUser(provider, providerUserId, email, name));
    }

    private User connectOrCreateUser(
            SocialProvider provider,
            String providerUserId,
            String email,
            String name
    ) {
        /*
         * 2순위:
         * oauth_account는 없지만 같은 email의 일반 회원이 있으면
         * 기존 User에 Google 계정을 연결합니다.
         *
         * 3순위:
         * 같은 email의 User도 없으면 소셜 회원을 자동 생성합니다.
         */
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User oauthUser = User.createOauthUser(email, name);
                    return userRepository.save(oauthUser);
                });

        /*
         * 이미 같은 provider가 연결되어 있는데 providerUserId가 다르면
         * 같은 User에 다른 Google 계정을 추가 연결하려는 상황입니다.
         *
         * 현재 DB에 uk_user_provider 제약이 있으므로 이 경우는 차단합니다.
         */
        oauthUserRepository.findByUserAndProvider(user, provider)
                .ifPresent(existingOauthUser -> {
                    throw new OAuth2AuthenticationException(
                            new OAuth2Error("oauth_account_already_linked"),
                            "이미 다른 Google 계정이 연결된 사용자입니다."
                    );
                });

        /*
         * User와 Google 계정 연결 정보를 oauth_account에 저장합니다.
         */
        OauthUser oauthUser = OauthUser.create(
                user,
                provider,
                providerUserId,
                email
        );

        oauthUserRepository.save(oauthUser);

        return user;
    }

    private SocialProvider resolveProvider(String registrationId) {
        if ("google".equalsIgnoreCase(registrationId)) {
            return SocialProvider.GOOGLE;
        }

        throw new OAuth2AuthenticationException(
                new OAuth2Error("unsupported_provider"),
                "지원하지 않는 소셜 로그인 제공자입니다."
        );
    }

    private String getRequiredAttribute(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);

        if (value == null || !StringUtils.hasText(String.valueOf(value))) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("missing_oauth_attribute"),
                    "Google 사용자 정보에 필수 값이 없습니다: " + key
            );
        }

        return String.valueOf(value);
    }

    private String resolveName(Object nameAttribute, String email) {
        if (nameAttribute != null && StringUtils.hasText(String.valueOf(nameAttribute))) {
            return String.valueOf(nameAttribute);
        }

        /*
         * name이 없으면 email 앞부분을 임시 이름으로 사용합니다.
         */
        int atIndex = email.indexOf("@");

        if (atIndex > 0) {
            return email.substring(0, atIndex);
        }

        return email;
    }

    private void validateEmailVerified(Map<String, Object> attributes) {
        Object emailVerified = attributes.get("email_verified");

        /*
         * Google에서 email_verified=false가 명시적으로 내려오면 차단합니다.
         * 값이 없는 경우는 provider 정책 차이 가능성이 있어 일단 통과시킵니다.
         */
        if (emailVerified != null && Boolean.FALSE.equals(emailVerified)) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("email_not_verified"),
                    "Google 이메일 인증이 완료되지 않은 계정입니다."
            );
        }
    }
}