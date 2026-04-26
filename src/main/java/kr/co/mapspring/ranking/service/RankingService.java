package kr.co.mapspring.ranking.service;

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
     *
     * 내 랭킹 조회
     *
     * [설명]
     * - 전체 랭킹 목록에서 특정 userId에 해당하는 사용자의 순위를 조회한다.
     *
     * [동장]
     * - 전체 랭킹 조회 후
     * - userId와 일치하는 데이터 필터링
     *
     * @param userId 랭킹을 조회할 사용자 ID
     * @return 해당 사용자의 랭킹 정보 (순위, userId, 총 점수)
     */
    RankingDto.ResponseRanking getMyRanking(Long userId);
}
