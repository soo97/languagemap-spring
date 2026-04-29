package kr.co.mapspring.favorite.repository;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, Long> {

    boolean existsByUser_UserIdAndPlace_PlaceId(Long userId, Long placeId);

    Optional<FavoritePlace> findByUser_UserIdAndPlace_PlaceId(Long userId, Long placeId);

    List<FavoritePlace> findAllByUser_UserId(Long userId);

}
