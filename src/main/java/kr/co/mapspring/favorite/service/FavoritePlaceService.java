package kr.co.mapspring.favorite.service;

public interface FavoritePlaceService {
    /**
     * 사용자의 즐겨찾기 장소를 추가한다.
     *
     * 동일한 장소는 중복해서 즐겨찾기할 수 없다.
     *
     * @param userId 사용자 ID
     * @param placeId 즐겨찾기할 장소 ID
     */
    void addFavoritePlace(Long userId, Long placeId);

    /**
     * 사용자의 즐겨찾기 장소를 삭제한다.
     *
     * @param userId 사용자 ID
     * @param placeId 삭제할 장소 ID
     */
    void removeFavoritePlace(Long userId, Long placeId);

}
