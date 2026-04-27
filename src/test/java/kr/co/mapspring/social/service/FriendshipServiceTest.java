package kr.co.mapspring.social.service;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.enums.FriendshipStatus;
import kr.co.mapspring.social.repository.FriendshipRepository;
import kr.co.mapspring.social.service.impl.FriendshipServiceImpl;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipServiceImpl friendshipService;

    @Test
    @DisplayName("친구 요청을 정상적으로 보낸다")
    void 친구_요청을_정상적으로_보낸다() {

        Long requesterId = 1L;
        Long addresseeId = 2L;

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(requester.getUserId()).willReturn(requesterId);
        given(addressee.getUserId()).willReturn(addresseeId);

        given(userRepository.findById(requesterId))
                .willReturn(Optional.of(requester));

        given(userRepository.findById(addresseeId))
                .willReturn(Optional.of(addressee));

        given(friendshipRepository.existsFriendshipBetween(requesterId, addresseeId))
                .willReturn(false);

        friendshipService.sendFriendRequest(requesterId, addresseeId);

        verify(userRepository).findById(requesterId);
        verify(userRepository).findById(addresseeId);
        verify(friendshipRepository).existsFriendshipBetween(requesterId, addresseeId);

        ArgumentCaptor<Friendship> captor = ArgumentCaptor.forClass(Friendship.class);
        verify(friendshipRepository).save(captor.capture());

        Friendship savedFriendship = captor.getValue();

        assertEquals(requester, savedFriendship.getRequester());
        assertEquals(addressee, savedFriendship.getAddressee());
        assertEquals(FriendshipStatus.PENDING, savedFriendship.getStatus());
    }

    @Test
    @DisplayName("자기 자신에게 친구 요청을 보낼 수 없다")
    void 자기_자신에게_친구_요청을_보낼_수_없다() {

        Long userId = 1L;

        assertThrows(CustomException.class,
                () -> friendshipService.sendFriendRequest(userId, userId));

        verify(userRepository, never()).findById(any(Long.class));
        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @Test
    @DisplayName("이미 친구 관계가 존재하면 친구 요청을 보낼 수 없다")
    void 이미_친구_관계가_존재하면_친구_요청을_보낼_수_없다() {

        Long requesterId = 1L;
        Long addresseeId = 2L;

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(userRepository.findById(requesterId))
                .willReturn(Optional.of(requester));

        given(userRepository.findById(addresseeId))
                .willReturn(Optional.of(addressee));

        given(friendshipRepository.existsFriendshipBetween(requesterId, addresseeId))
                .willReturn(true);

        assertThrows(CustomException.class,
                () -> friendshipService.sendFriendRequest(requesterId, addresseeId));
    }

    @Test
    @DisplayName("친구 요청을 정상적으로 수락한다")
    void 친구_요청을_정상적으로_수락한다() {

        Long friendshipId = 1L;
        Long addresseeId = 2L;

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(addressee.getUserId()).willReturn(addresseeId);

        Friendship friendship = Friendship.of(
                friendshipId,
                requester,
                addressee,
                FriendshipStatus.PENDING
        );

        given(friendshipRepository.findById(friendshipId))
                .willReturn(Optional.of(friendship));

        friendshipService.acceptFriendRequest(friendshipId, addresseeId);

        verify(friendshipRepository).findById(friendshipId);

        assertEquals(FriendshipStatus.ACCEPTED, friendship.getStatus());
    }

    @Test
    @DisplayName("친구 요청을 정상적으로 거절한다")
    void 친구_요청을_정상적으로_거절한다() {

        Long friendshipId = 1L;
        Long addresseeId = 2L;

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(addressee.getUserId()).willReturn(addresseeId);

        Friendship friendship = Friendship.of(
                friendshipId,
                requester,
                addressee,
                FriendshipStatus.PENDING
        );

        given(friendshipRepository.findById(friendshipId))
                .willReturn(Optional.of(friendship));

        friendshipService.rejectFriendRequest(friendshipId, addresseeId);

        verify(friendshipRepository).findById(friendshipId);

        assertEquals(FriendshipStatus.REJECTED, friendship.getStatus());
    }

    @Test
    @DisplayName("사용자의 친구 목록을 조회한다")
    void 사용자의_친구_목록을_조회한다() {

        Long userId = 1L;

        User user = mock(User.class);
        User friend1 = mock(User.class);
        User friend2 = mock(User.class);

        given(user.getUserId()).willReturn(userId);
        given(friend1.getUserId()).willReturn(2L);
        given(friend2.getUserId()).willReturn(3L);

        Friendship friendship1 = Friendship.of(
                1L,
                user,
                friend1,
                FriendshipStatus.ACCEPTED
        );

        Friendship friendship2 = Friendship.of(
                2L,
                friend2,
                user,
                FriendshipStatus.ACCEPTED
        );

        given(friendshipRepository.findAcceptedFriendshipsByUserId(userId))
                .willReturn(List.of(friendship1, friendship2));

        List<Friendship> result = friendshipService.getFriends(userId);

        verify(friendshipRepository).findAcceptedFriendshipsByUserId(userId);

        assertEquals(2, result.size());
        assertEquals(FriendshipStatus.ACCEPTED, result.get(0).getStatus());
        assertEquals(FriendshipStatus.ACCEPTED, result.get(2).getStatus());
    }

    @Test
    @DisplayName("친구 관계를 정상적으로 삭제한다")
    void 친구_관계를_정상적으로_삭제한다() {

        Long friendshipId = 1L;
        Long userId = 1L;

        User requester = mock(User.class);
        User addressee = mock(User.class);

        given(requester.getUserId()).willReturn(userId);
        given(addressee.getUserId()).willReturn(2L);

        Friendship friendship = Friendship.of(
                friendshipId,
                requester,
                addressee,
                FriendshipStatus.ACCEPTED
        );

        given(friendshipRepository.findById(friendshipId))
                .willReturn(Optional.of(friendship));

        friendshipService.deleteFriend(friendshipId, userId);

        verify(friendshipRepository).findById(friendshipId);
        verify(friendshipRepository).delete(friendship);
    }
}
