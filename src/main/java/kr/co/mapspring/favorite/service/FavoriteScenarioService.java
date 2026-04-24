package kr.co.mapspring.favorite.service;

import kr.co.mapspring.favorite.entity.FavoriteScenario;

import java.util.List;

public interface FavoriteScenarioService {
    /**
     * 사용자의 즐겨찾기 시나리오를 추가한다.
     *
     * 동일한 장소는 중복해서 즐겨찾기할 수 없다.
     *
     * @param userId 사용자 ID
     * @param scenarioId 즐겨찾기할 시나리오 ID
     */
    void addFavoriteScenario(Long userId, Long scenarioId);

    /**
     * 사용자의 즐겨찾기 시나리오를 삭제한다.
     *
     * @param userId 사용자 ID
     * @param scenarioId 삭제할 시나리오 ID
     */
    void removeFavoriteScenario(Long userId, Long scenarioId);

    /**
     * 사용자의 즐겨찾기 시나리오 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 즐겨찾기 시나리오 목록
     */
    List<FavoriteScenario> getFavoriteScenarios(Long userId);

}
