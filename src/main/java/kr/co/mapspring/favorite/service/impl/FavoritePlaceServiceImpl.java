package kr.co.mapspring.favorite.service.impl;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import kr.co.mapspring.favorite.repository.FavoritePlaceRepository;
import kr.co.mapspring.favorite.service.FavoritePlaceService;
import kr.co.mapspring.global.exception.favorite.FavoritePlaceAlreadyExistsException;
import kr.co.mapspring.global.exception.favorite.FavoritePlaceNotFoundException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoritePlaceServiceImpl implements FavoritePlaceService {

    private final FavoritePlaceRepository favoritePlaceRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void addFavoritePlace(Long userId, Long placeId) {
        boolean alreadyExists = favoritePlaceRepository.existsByUser_UserIdAndPlace_PlaceId(userId, placeId);

        if (alreadyExists) {
            throw new FavoritePlaceAlreadyExistsException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Place place = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);

        FavoritePlace favoritePlace = FavoritePlace.create(user, place);
        favoritePlaceRepository.save(favoritePlace);
    }

    @Override
    @Transactional
    public void removeFavoritePlace(Long userId, Long placeId) {
        FavoritePlace favoritePlace = favoritePlaceRepository.findByUser_UserIdAndPlace_PlaceId(userId, placeId)
                .orElseThrow(FavoritePlaceNotFoundException::new);

        favoritePlaceRepository.delete(favoritePlace);
    }

    @Override
    public List<FavoritePlace> getFavoritePlaces(Long userId) {
        return favoritePlaceRepository.findAllByUser_UserId(userId);
    }
}
