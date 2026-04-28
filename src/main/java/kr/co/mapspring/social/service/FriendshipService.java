package kr.co.mapspring.social.service;

import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.user.entity.User;

import java.util.List;

public interface FriendshipService {

    /**
     * 친구 요청 보내기
     *
     * [설명]
     * - requesterId가 addresseeId에게 친구 요청을 보낸다.
     *
     * [검증]
     * - 자기 자신에게 요청할 수 없다.
     * - 이미 친구 관계(PENDING, ACCEPTED)가 존재하면 요청할 수 없다.
     *
     * @param requesterId 친구 요청을 보내는 사용자 ID
     * @param addresseeId 친구 요청을 받는 사용자 ID
     */
    void sendFriendRequest(Long requesterId, Long addresseeId);

    /**
     * 이메일로 친구 요청
     *
     * [설명]
     * - 이메일을 통해 특정 사용자에게 친구 요청을 보낸다.
     * - 이메일로 사용자를 찾은 뒤 기존 친구 요청 로직을 수행한다.
     *
     * [검증]
     * - 요청 보내는 사용자가 존재해야 한다.
     * - 이메일에 해당하는 사용자가 존재해야 한다.
     * - 자기 자신에게 요청할 수 없다.
     * - 이미 친구 관계가 존재하면 요청할 수 없다.
     *
     * @param requesterId 요청 보내는 사용자 ID
     * @param email 요청 받는 사용자 이메일
     */
    void sendFriendRequestByEmail(Long requesterId, String email);

    /**
     * 친구 요청 수락
     *
     * [설명]
     * - addresseeId가 자신에게 온 친구 요청을 수락한다.
     *
     * [검증]
     * - 해당 요청이 존재해야 한다.
     * - 요청의 수신자가 addresseeId와 일치해야 한다.
     *
     * @param friendshipId 친구 요청 ID
     * @param addresseeId 요청을 수락하는 사용자 ID
     */
    void acceptFriendRequest(Long friendshipId, Long addresseeId);

    /**
     * 친구 요청 거절
     *
     * [설명]
     * - addresseeId가 자신에게 온 친구 요청을 거절한다.
     *
     * [검증]
     * - 해당 요청이 존재해야 한다.
     * - 요청의 수신자가 addresseeId와 일치해야 한다.
     *
     * @param friendshipId 친구 요청 ID
     * @param addresseeId 요청을 거절하는 사용자 ID
     */
    void rejectFriendRequest(Long friendshipId, Long addresseeId);

    /**
     * 친구 목록 조회
     *
     * [설명]
     * - userId와 친구 관계(ACCEPTED)인 사용자 목록을 조회한다.
     *
     * [동작]
     * - requester 또는 addressee에 userId가 포함된 친구 관계를 조회한다.
     * - 상태가 ACCEPTED인 경우만 반환된다.
     *
     * @param userId 친구 목록을 조회할 사용자 ID
     * @return 친구 관계 리스트
     */
    List<Friendship> getFriends(Long userId);

    /**
     * 친구 삭제
     *
     * [설명]
     * - userId와 관련된 친구 관계를 삭제한다.
     *
     * [검증]
     * - 해당 친구 관계가 존재해야 한다.
     * - userId가 해당 친구 관계의 당사자여야 한다.
     *
     * @param friendshipId 삭제할 친구 관계 ID
     * @param userId 친구 삭제를 요청한 사용자 ID
     */
    void deleteFriend(Long friendshipId, Long userId);

    /**
     * 받은 친구 요청 목록 조회
     *
     * [설명]
     * - 특정 사용자가 받은 친구 요청 목록을 조회한다.
     * - 아직 처리되지 않은 요청(PENDING 상태)만 조회한다.
     *
     * [검증]
     * - userId는 필수 값이다.
     *
     * @param userId 조회할 사용자 ID
     * @return 받은 친구 요청 목록 (PENDING 상태)
     */
    List<Friendship> getReceivedRequests(Long userId);

    /**
     * 보낸 친구 요청 목록 조회
     *
     * [설명]
     * - 특정 사용자가 보낸 친구 요청 목록을 조회한다.
     * - 아직 처리되지 않은 요청(PENDING 상태)만 조회한다.
     *
     * [검증]
     * - userId는 필수 값이다.
     *
     * @param userId 조회할 사용자 ID
     * @return 보낸 친구 요청 목록 (PENDING 상태)
     */
    List<Friendship> getSentRequests(Long userId);

    /**
     * 친구 차단
     *
     * [설명]
     * - 특정 친구 관계를 차단 상태로 변경한다.
     *
     * [검증]
     * - 해당 친구 관계가 존재해야 한다.
     * - userId가 해당 친구 관계의 당사자여야 한다.
     *
     * @param friendshipId 친구 관계 ID
     * @param userId 요청 사용자 ID
     */
    void blockFriend(Long friendshipId, Long userId);

    /**
     * 차단 및 거절 이력 조회
     *
     * [설명]
     * - 특정 사용자와 관련된 친구 관계 중 거절(REJECTED), 차단(BLOCKED) 상태의 이력을 조회한다.
     *
     * [검증]
     * - userId는 필수 값이다.
     *
     * @param userId 조회할 사용자 ID
     * @return 차단 및 거절 이력 목록
     */
    List<Friendship> getFriendshipHistory(Long userId);

    /**
     * 추천 친구 조회
     *
     * [설명]
     * - 친구 관계가 없는 사용자를 랜덤으로 추천한다.
     * - 자기 자신은 추천 대상에서 제외한다.
     *
     * [검증]
     * - userId는 필수 값이다.
     *
     * @param userId 추천 친구를 조회할 사용자 ID
     * @return 추천 친구 목록
     */
    List<User> getRecommendedFriends(Long userId);
}
