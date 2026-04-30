package kr.co.mapspring.user.entity;

import java.time.LocalDate;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;
	
	@Column(name = "email",unique = true, length = 100)
	private String email;
	
	@Column(name = "name", nullable = false, length = 50)
	private String name;
	
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;
	
	@Column(name = "address", nullable = false, length = 255)
    private String address;
	
	@Column(name = "phone_number", nullable = false, unique = true,length = 20)
    private String phoneNumber;
	
	@Column(name = "password_hash", length = 255)
	private String passwordHash;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private UserStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 20)
	private UserRole role;
	
	@Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
	
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

	
	@Builder
    private User(
            Long userId,
            String email,
            String name,
            LocalDate birthDate,
            String address,
            String phoneNumber,
            String passwordHash,
            UserStatus status,
            UserRole role
    ) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.status = status;
        this.role = role;
    }

    public static User create(
            String email,
            String name,
            LocalDate birthDate,
            String address,
            String phoneNumber,
            String passwordHash
    ) {
        return User.builder()
                .email(email)
                .name(name)
                .birthDate(birthDate)
                .address(address)
                .phoneNumber(phoneNumber)
                .passwordHash(passwordHash)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
    }

	
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }
    
    // 테스트 전용 메서드 
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

}
