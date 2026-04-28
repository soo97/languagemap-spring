package kr.co.mapspring.social.entity;


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
import jakarta.persistence.Table;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id", nullable = false, updatable = false)
    private Long friendshipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FriendshipStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
    }

    public static Friendship create(User requester, User addressee) {
        Friendship friendship = new Friendship();
        friendship.requester = requester;
        friendship.addressee = addressee;
        friendship.status = FriendshipStatus.PENDING;
        return friendship;
    }

    public void accept() {
        this.status = FriendshipStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = FriendshipStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void block() {
        this.status = FriendshipStatus.BLOCKED;
        this.respondedAt = LocalDateTime.now();
    }

    public boolean isAddressee(Long userId) {
        return this.addressee.getUserId().equals(userId);
    }

    public boolean isRelatedUser(Long userId) {
        return this.requester.getUserId().equals(userId) || this.addressee.getUserId().equals(userId);
    }

    // 테스트 전용 메서드
    public static Friendship of(
            Long friendshipId,
            User requester,
            User addressee,
            FriendshipStatus status
    ) {
        Friendship friendship = new Friendship();
        friendship.friendshipId = friendshipId;
        friendship.requester = requester;
        friendship.addressee = addressee;
        friendship.status = status;
        friendship.requestedAt = LocalDateTime.now();
        return friendship;
    }

}
