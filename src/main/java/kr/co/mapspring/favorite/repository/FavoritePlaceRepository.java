package kr.co.mapspring.favorite.repository;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

}
