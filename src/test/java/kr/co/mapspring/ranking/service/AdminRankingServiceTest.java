package kr.co.mapspring.ranking.service;

import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.service.impl.AdminRankingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminRankingServiceTest {

    @Mock
    private RankingService rankingService;

    @InjectMocks
    private AdminRankingServiceImpl adminRankingService;

    @Test
    @DisplayName("관리자는 전체 랭킹을 조회한다")
    void 관리자는_전체_랭킹을_조회한다() {

        RankingDto.ResponseRanking ranking1 =
                RankingDto.ResponseRanking.from(1, 1L, 300L);

        RankingDto.ResponseRanking ranking2 =
                RankingDto.ResponseRanking.from(2, 2L, 250L);

        given(rankingService.getRankings())
                .willReturn(List.of(ranking1, ranking2));

        List<RankingDto.ResponseRanking> result =
                adminRankingService.getRankings();

        verify(rankingService).getRankings();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getRank());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(300L, result.get(0).getTotalScore());
    }

    @Test
    @DisplayName("관리자는 주간 랭킹을 조회한다")
    void 관리자는_주간_랭킹을_조회한다() {

        RankingDto.ResponseRanking ranking1 =
                RankingDto.ResponseRanking.from(1, 1L, 150L);

        RankingDto.ResponseRanking ranking2 =
                RankingDto.ResponseRanking.from(2, 2L, 100L);

        given(rankingService.getWeeklyRankings())
                .willReturn(List.of(ranking1, ranking2));

        List<RankingDto.ResponseRanking> result =
                adminRankingService.getWeeklyRankings();

        verify(rankingService).getWeeklyRankings();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getRank());
        assertEquals(150L, result.get(0).getTotalScore());
    }

    @Test
    @DisplayName("관리자는 전체 사용자 수를 조회한다")
    void 관리자는_전체_사용자_수를_조회한다() {

        given(rankingService.getTotalUserCount())
                .willReturn(10L);

        Long result = adminRankingService.getTotalUserCount();

        verify(rankingService).getTotalUserCount();

        assertEquals(10L, result);
    }
}