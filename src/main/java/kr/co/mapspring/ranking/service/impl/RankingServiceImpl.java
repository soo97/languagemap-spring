package kr.co.mapspring.ranking.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.co.mapspring.global.exception.ranking.RankingNotFoundException;
import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.repository.RankingRepository;
import kr.co.mapspring.ranking.service.RankingService;
import kr.co.mapspring.social.entity.Friendship;
import kr.co.mapspring.social.repository.FriendshipRepository;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Override
    public List<RankingDto.ResponseRanking> getRankings() {

        List<Object[]> rankingResult = rankingRepository.findRanking();

        return IntStream.range(0, rankingResult.size())
                .mapToObj(index -> {
                    Object[] row = rankingResult.get(index);

                    Long userId = (Long) row[0];
                    Long totalScore = (Long) row[1];

                    return RankingDto.ResponseRanking.from(
                            index + 1,
                            userId,
                            totalScore
                    );
                })
                .toList();
    }

    @Override
    public RankingDto.ResponseRanking getMyRanking(Long userId) {

        return getRankings().stream()
                .filter(ranking -> ranking.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(RankingNotFoundException::new);
    }

    @Override
    public List<RankingDto.ResponseRanking> getFriendRankings(Long userId) {

        List<Long> friendIds = getFriendIds(userId);

        if (friendIds.isEmpty()) {
            return List.of();
        }

        List<Object[]> rankingResult = rankingRepository.findFriendRanking(friendIds);

        return convertToRankingResponse(rankingResult);
    }

    @Override
    public Long getFriendBestScore(Long userId) {

        List<Long> friendIds = getFriendIds(userId);

        if (friendIds.isEmpty()) {
            return 0L;
        }

        return rankingRepository.findFriendBestScore(friendIds);
    }

    @Override
    public Double getFriendAverageScore(Long userId) {

        List<Long> friendIds = getFriendIds(userId);

        if (friendIds.isEmpty()) {
            return 0.0;
        }

        return rankingRepository.findFriendAverageScore(friendIds);
    }

    @Override
    public List<RankingDto.ResponseRanking> getWeeklyRankings() {

        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);

        List<Object[]> rankingResult = rankingRepository.findWeeklyRanking(startDateTime);

        return convertToRankingResponse(rankingResult);
    }

    @Override
    public Long getTotalUserCount() {

        return userRepository.count();
    }

    private List<Long> getFriendIds(Long userId) {

        List<Friendship> friendships =
                friendshipRepository.findAcceptedFriendshipsByUserId(userId);

        return friendships.stream()
                .map(friendship -> {
                    Long requesterId = friendship.getRequester().getUserId();
                    Long addresseeId = friendship.getAddressee().getUserId();

                    if (requesterId.equals(userId)) {
                        return addresseeId;
                    }

                    return requesterId;
                })
                .toList();
    }

    private List<RankingDto.ResponseRanking> convertToRankingResponse(List<Object[]> rankingResult) {

        return IntStream.range(0, rankingResult.size())
                .mapToObj(index -> {
                    Object[] row = rankingResult.get(index);

                    Long userId = (Long) row[0];
                    Long totalScore = (Long) row[1];

                    return RankingDto.ResponseRanking.from(
                            index + 1,
                            userId,
                            totalScore
                    );
                })
                .toList();
    }
}