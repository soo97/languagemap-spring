package kr.co.mapspring.ranking.service;

import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.repository.RankingRepository;
import kr.co.mapspring.ranking.service.impl.RankingServiceImpl;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.repository.FriendshipRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RankingServiceImpl rankingService;

    @Test
    @DisplayName("전체 랭킹을 점수 내림차순으로 조회한다")
    void 전체_랭킹을_점수_내림차순으로_조회한다() {

        List<Object[]> rankingResult = List.of(
                new Object[]{1L, 300L},
                new Object[]{2L, 200L},
                new Object[]{3L, 100L}
        );

        given(rankingRepository.findRanking())
                .willReturn(rankingResult);

        List<RankingDto.ResponseRanking> result = rankingService.getRankings();

        verify(rankingRepository).findRanking();

        assertEquals(3, result.size());

        assertEquals(1, result.get(0).getRank());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(300L, result.get(0).getTotalScore());

        assertEquals(2, result.get(1).getRank());
        assertEquals(2L, result.get(1).getUserId());
        assertEquals(200L, result.get(1).getTotalScore());

        assertEquals(3, result.get(2).getRank());
        assertEquals(3L, result.get(2).getUserId());
        assertEquals(100L, result.get(2).getTotalScore());
    }

    @Test
    @DisplayName("내 랭킹을 정상적으로 조회한다")
    void 내_랭킹을_정상적으로_조회한다() {

        Long userId = 2L;

        List<Object[]> rankingResult = List.of(
                new Object[]{1L, 300L},
                new Object[]{2L, 200L},
                new Object[]{3L, 100L}
        );

                given(rankingRepository.findRanking())
                        .willReturn(rankingResult);

        RankingDto.ResponseRanking result = rankingService.getMyRanking(userId);

        verify(rankingRepository).findRanking();

        assertEquals(2, result.getRank());
        assertEquals(userId, result.getUserId());
        assertEquals(200L, result.getTotalScore());
    }

    @Test
    @DisplayName("친구 랭킹을 점수 내림차순으로 조회한다")
    void 친구_랭킹을_점수_내림차순으로_조회한다() {

        Long userId = 1L;

        User me = mock(User.class);
        User friend1 = mock(User.class);
        User friend2 = mock(User.class);

        Friendship friendship1 = mock(Friendship.class);
        Friendship friendship2 = mock(Friendship.class);

        given(me.getUserId()).willReturn(1L);
        given(friend1.getUserId()).willReturn(2L);
        given(friend2.getUserId()).willReturn(3L);

        // 내가 요청자, 상대가 친구
        given(friendship1.getRequester()).willReturn(me);
        given(friendship1.getAddressee()).willReturn(friend1);

        // 상대가 요청자, 내가 수락자
        given(friendship2.getRequester()).willReturn(friend2);
        given(friendship2.getAddressee()).willReturn(me);

        List<Friendship> friendships = List.of(friendship1, friendship2);
        List<Long> friendIds = List.of(2L, 3L);

        List<Object[]> rankingResult = List.of(
                new Object[]{2L, 250L},
                new Object[]{3L, 150L}
        );

        given(friendshipRepository.findAcceptedFriendshipsByUserId(userId))
                .willReturn(friendships);

        given(rankingRepository.findFriendRanking(friendIds))
                .willReturn(rankingResult);

        List<RankingDto.ResponseRanking> result = rankingService.getFriendRankings(userId);

        verify(friendshipRepository).findAcceptedFriendshipsByUserId(userId);
        verify(rankingRepository).findFriendRanking(friendIds);

        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getRank());
        assertEquals(2L, result.get(0).getUserId());
        assertEquals(250L, result.get(0).getTotalScore());

        assertEquals(2, result.get(1).getRank());
        assertEquals(3L, result.get(1).getUserId());
        assertEquals(150L, result.get(1).getTotalScore());
    }

    @Test
    @DisplayName("친구 최고 점수를 조회한다")
    void 친구_최고_점수를_조회한다() {

        Long userId = 1L;

        User me = mock(User.class);
        User friend1 = mock(User.class);
        User friend2 = mock(User.class);

        Friendship friendship1 = mock(Friendship.class);
        Friendship friendship2 = mock(Friendship.class);

        given(me.getUserId()).willReturn(1L);
        given(friend1.getUserId()).willReturn(2L);
        given(friend2.getUserId()).willReturn(3L);

        given(friendship1.getRequester()).willReturn(me);
        given(friendship1.getAddressee()).willReturn(friend1);

        given(friendship2.getRequester()).willReturn(friend2);
        given(friendship2.getAddressee()).willReturn(me);

        List<Friendship> friendships = List.of(friendship1, friendship2);
        List<Long> friendIds = List.of(2L, 3L);

        given(friendshipRepository.findAcceptedFriendshipsByUserId(userId))
                .willReturn(friendships);

        given(rankingRepository.findFriendBestScore(friendIds))
                .willReturn(95L);

        Long result = rankingService.getFriendBestScore(userId);

        verify(friendshipRepository).findAcceptedFriendshipsByUserId(userId);
        verify(rankingRepository).findFriendBestScore(friendIds);

        assertEquals(95L, result);
    }

    @Test
    @DisplayName("친구 평균 점수를 조회한다")
    void 친구_평균_점수를_조회한다() {

        Long userId = 1L;

        User me = mock(User.class);
        User friend1 = mock(User.class);
        User friend2 = mock(User.class);

        Friendship friendship1 = mock(Friendship.class);
        Friendship friendship2 = mock(Friendship.class);

        given(me.getUserId()).willReturn(1L);
        given(friend1.getUserId()).willReturn(2L);
        given(friend2.getUserId()).willReturn(3L);

        given(friendship1.getRequester()).willReturn(me);
        given(friendship1.getAddressee()).willReturn(friend1);

        given(friendship2.getRequester()).willReturn(friend2);
        given(friendship2.getAddressee()).willReturn(me);

        List<Friendship> friendships = List.of(friendship1, friendship2);
        List<Long> friendIds = List.of(2L, 3L);

        given(friendshipRepository.findAcceptedFriendshipsByUserId(userId))
                .willReturn(friendships);

        given(rankingRepository.findFriendAverageScore(friendIds))
                .willReturn(82.5);

        Double result = rankingService.getFriendAverageScore(userId);

        verify(friendshipRepository).findAcceptedFriendshipsByUserId(userId);
        verify(rankingRepository).findFriendAverageScore(friendIds);

        assertEquals(82.5, result);
    }

    @Test
    @DisplayName("전체 사용자 수를 조회한다")
    void 전체_사용자_수를_조회한다() {

        given(userRepository.count())
                .willReturn(10L);

        Long result = rankingService.getTotalUserCount();

        verify(userRepository).count();

        assertEquals(10L, result);
    }

    @Test
    @DisplayName("주간 랭킹을 점수 내림차순으로 조회한다")
    void 주간_랭킹을_점수_내림차순으로_조회한다() {

        List<Object[]> rankingResult = List.of(
                new Object[]{1L, 180L},
                new Object[]{2L, 120L}
        );

        given(rankingRepository.findWeeklyRanking(any(LocalDateTime.class)))
                .willReturn(rankingResult);

        List<RankingDto.ResponseRanking> result = rankingService.getWeeklyRankings();

        verify(rankingRepository).findWeeklyRanking(any(LocalDateTime.class));

        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getRank());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(180L, result.get(0).getTotalScore());

        assertEquals(2, result.get(1).getRank());
        assertEquals(2L, result.get(1).getUserId());
        assertEquals(120L, result.get(1).getTotalScore());
    }

}
