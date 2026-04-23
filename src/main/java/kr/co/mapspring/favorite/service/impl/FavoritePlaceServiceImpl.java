package kr.co.mapspring.favorite.service.impl;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import kr.co.mapspring.favorite.repository.FavoritePlaceRepository;
import kr.co.mapspring.favorite.service.FavoritePlaceService;
import kr.co.mapspring.global.exception.favorite.FavoritePlaceAlreadyExistsException;
import kr.co.mapspring.global.exception.favorite.FavoritePlaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoritePlaceServiceImpl implements FavoritePlaceService {

    private final FavoritePlaceRepository favoritePlaceRepository;

    @Override
    @Transactional
    public void addFavoritePlace(Long userId, Long placeId) {
        boolean alreadyExists = favoritePlaceRepository.existsByUserIdAndPlaceId(userId, placeId);

        if (alreadyExists) {
            throw new FavoritePlaceAlreadyExistsException();
        }

        FavoritePlace favoritePlace = FavoritePlace.create(userId, placeId);
        favoritePlaceRepository.save(favoritePlace);
    }

    @Override
    @Transactional
    public void removeFavoritePlace(Long userId, Long placeId) {
        FavoritePlace favoritePlace = favoritePlaceRepository.findByUserIdAndPlaceId(userId, placeId)
                .orElseThrow(FavoritePlaceNotFoundException::new);

        favoritePlaceRepository.delete(favoritePlace);
    }

    @Override
    public List<FavoritePlace> getFavoritePlaces(Long userId) {
        return favoritePlaceRepository.findAllByUserId(userId);
    }
}
