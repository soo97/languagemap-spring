package kr.co.mapspring.social.service.impl;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.repository.FriendshipRepository;
import kr.co.mapspring.social.service.FriendshipService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipServiceImpl implements FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    @Transactional
    public void sendFriendRequest(Long requesterId, Long addresseeId) {

        if (requesterId.equals(addresseeId)) {
            throw new CustomException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_FOR_SOCIAL));

        User addressee = userRepository.findById(addresseeId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_FOR_SOCIAL));

        if (friendshipRepository.existsFriendshipBetween(requesterId, addresseeId)) {
            throw new CustomException(ErrorCode.FRIENDSHIP_ALREADY_EXISTS);
        }

        Friendship friendship = Friendship.create(requester, addressee);

        friendshipRepository.save(friendship);
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Long friendshipId, Long addresseeId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (!friendship.isAddressee(addresseeId)) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ACCESS_DENIED);
        }

        friendship.accept();
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Long friendshipId, Long addresseeId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (!friendship.isAddressee(addresseeId)) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ACCESS_DENIED);
        }

        friendship.reject();
    }

    @Override
    public List<Friendship> getFriends(Long userId) {

        return friendshipRepository.findAcceptedFriendshipsByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteFriend(Long friendshipId, Long userId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (!friendship.isRelatedUser(userId)) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ACCESS_DENIED);
        }

        friendshipRepository.delete(friendship);
    }
}
