package kr.co.mapspring.social.repository;

import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("""
           SELECT COUNT(f) > 0
           FROM Friendship f
           WHERE (f.requester.userId = :requesterId AND f.addressee.userId = :addresseeId)
              OR (f.requester.userId = :addresseeId AND f.addressee.userId = :requesterId)            
           """)
    boolean existsFriendshipBetween(Long requesterId, Long addresseeId);

    @Query("""
           SELECT f
           FROM Friendship f
           WHERE (f.requester.userId = :userId OR f.addressee.userId = :userId)
             AND f.status = :status           
           """)
    List<Friendship> findFriendshipsByUserIdAndStatus(Long userId, FriendshipStatus status);

    default List<Friendship> findAcceptedFriendshipsByUserId(Long userId) {
        return findFriendshipsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
    }
}
