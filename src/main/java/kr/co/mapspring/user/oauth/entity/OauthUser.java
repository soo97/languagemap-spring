package kr.co.mapspring.user.oauth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.oauth.enums.SocialProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "oauth_account",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_provider_provider_user_id",
                        columnNames = {"provider", "provider_user_id"}
                ),
                @UniqueConstraint(
                        name = "uk_user_provider",
                        columnNames = {"user_id", "provider"}
                )
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_account_id", nullable = false, updatable = false)
    private Long oauthAccountId;

    /*
     * 우리 서비스의 User와 외부 OAuth 계정을 연결합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
     * 소셜 로그인 제공자입니다.
     * 예: GOOGLE, KAKAO
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    private SocialProvider provider;

    /*
     * Google에서 내려주는 고유 사용자 ID입니다.
     * Google OAuth에서는 보통 sub 값이 여기에 들어갑니다.
     *
     * 단독 unique = true는 제거합니다.
     * provider + provider_user_id 조합으로 unique를 거는 것이 더 정확합니다.
     */
    @Column(name = "provider_user_id", nullable = false, length = 100)
    private String providerUserId;

    /*
     * Google 계정 이메일입니다.
     * Google 계정 이메일이 변경될 가능성을 고려해 갱신 메서드를 둡니다.
     */
    @Column(name = "provider_email", length = 100)
    private String providerEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private OauthUser(
            Long oauthAccountId,
            User user,
            SocialProvider provider,
            String providerUserId,
            String providerEmail
    ) {
        this.oauthAccountId = oauthAccountId;
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.providerEmail = providerEmail;
    }

    /*
     * OAuth 계정 연결 정보 생성 메서드입니다.
     * Service에서는 setter 대신 이 create 메서드를 사용합니다.
     */
    public static OauthUser create(
            User user,
            SocialProvider provider,
            String providerUserId,
            String providerEmail
    ) {
        return OauthUser.builder()
                .user(user)
                .provider(provider)
                .providerUserId(providerUserId)
                .providerEmail(providerEmail)
                .build();
    }

    /*
     * Google 이메일이 변경되었을 수 있으므로
     * 로그인 시 최신 providerEmail로 갱신합니다.
     */
    public void updateProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}