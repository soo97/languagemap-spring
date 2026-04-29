package kr.co.mapspring.ranking.service;

import kr.co.mapspring.ranking.dto.RankingDto;

import java.util.List;

public interface RankingService {

    /**
     * 전체 랭킹 조회
     *
     * [설명]
     * - study_log 테이블을 기반으로 사용자별 score 합계를 계산하여
     *   점수가 높은 순으로 랭킹을 반환한다.
     *
     * [동작]
     * - userId 기준으로 score SUM 집계
     * - 점수 내림차순 정렬
     * - 순위(rank)는 Service에서 생성
     *
     * @return 사용자별 랭킹 리스트 (순위, userId, 총 점수 포함)
     */
    List<RankingDto.ResponseRanking> getRankings();

    /**
     * 내 랭킹 조회
     *
     * [설명]
     * - 전체 랭킹 목록에서 특정 userId에 해당하는 사용자의 순위를 조회한다.
     *
     * [동작]
     * - 전체 랭킹 조회 후
     * - userId와 일치하는 데이터 필터링
     *
     * @param userId 랭킹을 조회할 사용자 ID
     * @return 해당 사용자의 랭킹 정보 (순위, userId, 총 점수)
     */
    RankingDto.ResponseRanking getMyRanking(Long userId);

    /**
     * 친구 랭킹 조회
     *
     * [설명]
     * - 사용자의 친구 목록을 기준으로 친구들의 랭킹을 조회한다.
     *
     * [동작]
     * - ACCEPTED 상태의 친구 관계 조회
     * - 친구 userId 목록 추출
     * - 해당 친구들의 총 점수 집계 후 랭킹 생성
     *
     * @param userId 기준 사용자 ID
     * @return 친구 랭킹 리스트 (순위, userId, 총 점수 포함)
     */
    List<RankingDto.ResponseRanking> getFriendRankings(Long userId);

    /**
     * 친구 최고 점수 조회
     *
     * [설명]
     * - 사용자의 친구들 중 가장 높은 점수를 조회한다.
     *
     * [동작]
     * - 친구 userId 목록 조회
     * - totalScore 중 최대값 반환
     *
     * @param userId 기준 사용자 ID
     * @return 친구 최고 점수
     */
    Long getFriendBestScore(Long userId);

    /**
     * 친구 평균 점수 조회
     *
     * [설명]
     * - 사용자의 친구들의 평균 점수를 조회한다.
     *
     * [동작]
     * - 친구 userId 목록 조회
     * - totalScore 평균값 계산
     *
     * @param userId 기준 사용자 ID
     * @return 친구 평균 점수
     */
    Double getFriendAverageScore(Long userId);

    /**
     * 주간 랭킹 조회
     *
     * [설명]
     * - 최근 7일 기준으로 사용자 랭킹을 조회한다.
     *
     * [동작]
     * - created_at 기준 최근 7일 데이터 필터링
     * - 사용자별 totalScore 집계
     * - 점수 내림차순 정렬 후 랭킹 생성
     *
     * @return 주간 랭킹 리스트 (순위, userId, 총 점수 포함)
     */
    List<RankingDto.ResponseRanking> getWeeklyRankings();

    /**
     * 전체 사용자 수 조회
     *
     * [설명]
     * - 전체 사용자 수를 조회한다.
     *
     * [동작]
     * - user 테이블 count 조회
     *
     * @return 전체 사용자 수
     */
    Long getTotalUserCount();
}
