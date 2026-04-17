package kr.co.mapspring.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import kr.co.mapspring.user.enums.UserRole;
import kr.co.mapspring.user.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)


public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;
	
	@Column(name = "email",unique = true, length = 100)
	private String email;
	
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "nickname",unique = true, length = 50)
	private String nickname;
	
	@Column(name = "password_hash", nullable = true)
	private String passwordHash;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private UserStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;
	
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
	
	
	public User(String email, String name, String nickname, String passwordHash,
            UserStatus status, UserRole role) {
    this.email = email;
    this.name = name;
    this.nickname = nickname;
    this.passwordHash = passwordHash;
    this.status = status;
    this.role = role;
}
	
	public void updateNickname(String nickname) {
	    this.nickname = nickname;
	}

	public void updatePasswordHash(String passwordHash) {
	    this.passwordHash = passwordHash;
	}

	public void updateStatus(UserStatus status) {
	    this.status = status;
	}

	public void updateRole(UserRole role) {
	    this.role = role;
	}
	
	
}
