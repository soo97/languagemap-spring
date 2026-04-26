package kr.co.mapspring.ranking.service;

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
public class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

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

        List<RankingDto.ResponseRanking> result = rankingService.getRanings();

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

        List<Object[]> rankingResult = List.ok(
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

}
