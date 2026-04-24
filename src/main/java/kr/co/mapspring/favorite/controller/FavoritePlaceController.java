package kr.co.mapspring.favorite.controller;

import kr.co.mapspring.favorite.controller.docs.FavoritePlaceControllerDocs;
import kr.co.mapspring.favorite.dto.FavoritePlaceDto;
import kr.co.mapspring.favorite.entity.FavoritePlace;
import kr.co.mapspring.favorite.service.FavoritePlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites/places")
@RequiredArgsConstructor
public class FavoritePlaceController implements FavoritePlaceControllerDocs {

    private final FavoritePlaceService favoritePlaceService;

    @Override
    @PostMapping
    public void addFavoritePlace(@RequestBody FavoritePlaceDto.RequestAddFavoritePlace request) {
        favoritePlaceService.addFavoritePlace(
                request.getUserId(),
                request.getPlaceId()
        );
    }

    @Override
    @DeleteMapping
    public void removeFavoritePlace(@RequestBody FavoritePlaceDto.RequestRemoveFavoritePlace request) {
        favoritePlaceService.removeFavoritePlace(
                request.getUserId(),
                request.getPlaceId()
        );
    }

    @Override
    @GetMapping
    public List<FavoritePlaceDto.ResponseFavoritePlace> getFavoritePlaces(@RequestParam Long userId) {
        List<FavoritePlace> favoritePlaces = favoritePlaceService.getFavoritePlaces(userId);

        return favoritePlaces.stream()
                .map(FavoritePlaceDto.ResponseFavoritePlace::from)
                .toList();
    }
}
