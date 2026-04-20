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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	    name = "oauth_account",
	    uniqueConstraints = {
	        @UniqueConstraint(name = "uk_provider_provider_user_id", columnNames = {"provider", "provider_user_id"}),
	        @UniqueConstraint(name = "uk_user_provider", columnNames = {"user_id", "provider"})
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider", nullable = false, length = 20)
	private SocialProvider provider;
	
	@Column(name = "provider_user_id",unique = true, length = 100)
	private String providerUserId;
	
	@Column(name = "provider_email", length = 100)
	private String providerEmail;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
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
