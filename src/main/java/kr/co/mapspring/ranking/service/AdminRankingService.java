package kr.co.mapspring.ranking.service;

import kr.co.mapspring.ranking.dto.RankingDto;

import java.util.List;

public interface AdminRankingService {

    /**
     * 관리자가 전체 랭킹을 조회한다.
     *
     * 전체 사용자의 누적 점수 기준 랭킹을 조회한다.
     *
     * @return 전체 랭킹 목록
     */
    List<RankingDto.ResponseRanking> getRankings();

    /**
     * 관리자가 주간 랭킹을 조회한다.
     *
     * 최근 7일 기준 사용자의 점수 랭킹을 조회한다.
     *
     * @return 주간 랭킹 목록
     */
    List<RankingDto.ResponseRanking> getWeeklyRankings();

    /**
     * 관리자가 전체 사용자 수를 조회한다.
     *
     * @return 전체 사용자 수
     */
    Long getTotalUserCount();

}
