package kr.co.mapspring.social.repository;

import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
           SELECT f
           FROM Friendship f
           WHERE f.addressee.userId = :userId
           AND f.status = :status
           """)
    List<Friendship> findReceivedRequestsByUserIdAndStatus(Long userId, FriendshipStatus status);

    default List<Friendship> findReceivedRequestsByUserId(Long userId) {
        return findReceivedRequestsByUserIdAndStatus(userId, FriendshipStatus.PENDING);
    }

    @Query("""
           SELECT f
           FROM Friendship f
           WHERE f.requester.userId = :userId
           AND f.status = :status
           """)
    List<Friendship> findSentRequestsByUserIdAndStatus(Long userId, FriendshipStatus status);

    default List<Friendship> findSentRequestsByUserId(Long userId) {
        return findSentRequestsByUserIdAndStatus(userId, FriendshipStatus.PENDING);
    }

    @Query("""
           SELECT f
           FROM Friendship f
           WHERE (f.requester.userId = :userId OR f.addressee.userId = :userId)
           AND f.status IN (kr.co.mapspring.social.enums.FriendshipStatus.REJECTED,
                            kr.co.mapspring.social.enums.FriendshipStatus.BLOCKED)   
           """)
    List<Friendship> findHistoryByUserId(Long userId);

    @Query(value = """
        SELECT *
        FROM user u
        WHERE u.user_id != :userId
          AND NOT EXISTS (
              SELECT 1
              FROM friendship f
              WHERE (f.requester_id = :userId AND f.addressee_id = u.user_id)
                 OR (f.requester_id = u.user_id AND f.addressee_id = :userId)
          )
        ORDER BY RAND()
        LIMIT 5
        """, nativeQuery = true)
    List<User> findRandomRecommendedUsers(@Param("userId") Long userId);

    List<Friendship> findAllByStatusOrderByRespondedAtDesc(FriendshipStatus status);
}
