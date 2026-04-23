package kr.co.mapspring.social.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.mapspring.social.enums.FriendshipStatus;
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

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "addressee_id", nullable = false)
    private Long addresseeId;

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

    public static Friendship create(Long requesterId, Long addresseeId) {
        Friendship friendship = new Friendship();
        friendship.requesterId = requesterId;
        friendship.addresseeId = addresseeId;
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

    public boolean isAddreess(Long userId) {
        return this.addresseeId.equals(userId);
    }

    public boolean isRelatedUser(Long userId) {
        return this.requesterId.equals(userId) || this.addresseeId.equals(userId);
    }

    // 테스트 전용 메서드
    public static Friendship of(
            Long frienshipId,
            Long requesterId,
            Long addresseeId,
            FriendshipStatus status
    ) {
        Friendship friendship = new Friendship();
        friendship.friendshipId = frienshipId;
        friendship.requesterId = requesterId;
        friendship.addresseeId = addresseeId;
        friendship.status = status;
        friendship.requestedAt = LocalDateTime.now();
        return friendship;
    }

}
