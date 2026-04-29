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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone_number", unique = true, length = 20)
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

    /*
     * 일반 회원가입용 생성 메서드
     * 일반 회원가입에서는 SignUpDto와 SignUpService에서 필수값을 검증한다.
     */
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

    /*
     * Google OAuth 소셜 회원가입용 생성 메서드
     * Google에서 받을 수 없는 정보는 null로 둔다.
     * 이후 프로필 수정/추가 입력에서 birthDate, address, phoneNumber를 채운다.
     */
    public static User createOauthUser(
            String email,
            String name
    ) {
        return User.builder()
                .email(email)
                .name(name)
                .birthDate(null)
                .address(null)
                .phoneNumber(null)
                .passwordHash(null)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
    }

    /*
     * 소셜 회원가입 후 추가 프로필 입력이 필요한지 판단한다.
     * 프론트는 OAuth 로그인 성공 redirect에서 이 값을 보고 프로필 입력 페이지로 보낼 수 있다.
     */
    public boolean isProfileIncomplete() {
        return this.birthDate == null
                || this.address == null
                || this.phoneNumber == null;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    // 테스트 전용 메서드
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }
}