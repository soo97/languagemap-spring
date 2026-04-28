package kr.co.mapspring.social.service.impl;

import kr.co.mapspring.global.exception.social.EmailUserNotFoundException;
import kr.co.mapspring.global.exception.social.FriendRequestAccessDeniedException;
import kr.co.mapspring.global.exception.social.FriendshipAlreadyExistsException;
import kr.co.mapspring.global.exception.social.FriendshipNotFoundException;
import kr.co.mapspring.global.exception.social.InvalidSocialUserException;
import kr.co.mapspring.global.exception.social.SelfFriendRequestNotAllowedException;
import kr.co.mapspring.global.exception.social.UserNotFoundForSocialException;
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
            throw new SelfFriendRequestNotAllowedException();
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(UserNotFoundForSocialException::new);

        User addressee = userRepository.findById(addresseeId)
                .orElseThrow(UserNotFoundForSocialException::new);

        if (friendshipRepository.existsFriendshipBetween(requesterId, addresseeId)) {
            throw new FriendshipAlreadyExistsException();
        }

        Friendship friendship = Friendship.create(requester, addressee);

        friendshipRepository.save(friendship);
    }

    @Override
    @Transactional
    public void sendFriendRequestByEmail(Long requesterId, String email) {

        User addressee = userRepository.findByEmail(email)
                .orElseThrow(EmailUserNotFoundException::new);

        sendFriendRequest(requesterId, addressee.getUserId());
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Long friendshipId, Long addresseeId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.isAddressee(addresseeId)) {
            throw new FriendRequestAccessDeniedException();
        }

        friendship.accept();
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Long friendshipId, Long addresseeId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.isAddressee(addresseeId)) {
            throw new FriendRequestAccessDeniedException();
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
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.isRelatedUser(userId)) {
            throw new FriendRequestAccessDeniedException();
        }

        friendshipRepository.delete(friendship);
    }

    @Override
    public List<Friendship> getReceivedRequests(Long userId) {
        return friendshipRepository.findReceivedRequestsByUserId(userId);
    }

    @Override
    public List<Friendship> getSentRequests(Long userId) {

        if (userId == null) {
            throw new InvalidSocialUserException();
        }

        return friendshipRepository.findSentRequestsByUserId(userId);
    }

    @Override
    @Transactional
    public void blockFriend(Long friendshipId, Long userId) {

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.isRelatedUser(userId)) {
            throw new FriendRequestAccessDeniedException();
        }

        friendship.block();
    }

    @Override
    public List<Friendship> getFriendshipHistory(Long userId) {

        if (userId == null) {
            throw new InvalidSocialUserException();
        }

        return friendshipRepository.findHistoryByUserId(userId);
    }

    @Override
    public List<User> getRecommendedFriends(Long userId) {

        if (userId == null) {
            throw new InvalidSocialUserException();
        }

        return friendshipRepository.findRandomRecommendedUsers(userId);
    }
}