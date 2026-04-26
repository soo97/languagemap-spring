package kr.co.mapspring.ranking.service.impl;

import jakarta.transaction.Transactional;
import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.repository.RankingRepository;
import kr.co.mapspring.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;

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
                .orElse(null);
    }
}