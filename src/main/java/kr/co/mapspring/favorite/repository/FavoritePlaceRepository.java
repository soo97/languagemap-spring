package kr.co.mapspring.favorite.repository;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Optional<FavoritePlace> findByUserIdAndPlaceId(Long userId, Long placeId);

    List<FavoritePlace> findAllByUserId(Long userId);

}
