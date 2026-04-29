package kr.co.mapspring.user.oauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.oauth.entity.OauthUser;
import kr.co.mapspring.user.oauth.enums.SocialProvider;

public interface OauthUserRepository extends JpaRepository<OauthUser, Long> {

    /*
     * provider + providerUserId 조합으로 이미 연결된 소셜 계정인지 조회한다.
     * 예: GOOGLE + Google sub 값
     */
    Optional<OauthUser> findByProviderAndProviderUserId(
            SocialProvider provider,
            String providerUserId
    );

    /*
     * 특정 User가 특정 provider 계정을 이미 연결했는지 조회한다.
     * 현재 oauth_account 테이블에 uk_user_provider 제약이 있으므로
     * 한 User는 provider별로 하나의 계정만 연결할 수 있다.
     */
    Optional<OauthUser> findByUserAndProvider(
            User user,
            SocialProvider provider
    );

    /*
     * 단순 존재 여부 확인용.
     */
    boolean existsByUserAndProvider(
            User user,
            SocialProvider provider
    );
}