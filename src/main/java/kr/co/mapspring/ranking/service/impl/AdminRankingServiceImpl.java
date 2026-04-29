package kr.co.mapspring.ranking.service.impl;

import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.service.AdminRankingService;
import kr.co.mapspring.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminRankingServiceImpl implements AdminRankingService {

    private final RankingService rankingService;

    @Override
    public List<RankingDto.ResponseRanking> getRankings() {
        return rankingService.getRankings();
    }

    @Override
    public List<RankingDto.ResponseRanking> getWeeklyRankings() {
        return rankingService.getWeeklyRankings();
    }

    @Override
    public Long getTotalUserCount() {
        return rankingService.getTotalUserCount();
    }
}
