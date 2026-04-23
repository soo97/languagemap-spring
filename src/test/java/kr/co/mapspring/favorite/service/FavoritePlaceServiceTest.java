package kr.co.mapspring.favorite.service;

import kr.co.mapspring.favorite.entity.FavoritePlace;
import kr.co.mapspring.favorite.repository.FavoritePlaceRepository;
import kr.co.mapspring.favorite.service.impl.FavoritePlaceServiceImpl;
import kr.co.mapspring.global.exception.favorite.FavoritePlaceAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoritePlaceServiceTest {

    @Mock
    private FavoritePlaceRepository favoritePlaceRepository;

    @InjectMocks
    private FavoritePlaceServiceImpl favoritePlaceService;

    @Test
    @DisplayName("즐겨찾기 장소를 정상적으로 추가한다")
    void 즐겨찾기_장소를_정상적으로_추가한다() {

        Long userId = 1L;
        Long placeId = 10L;

        given(favoritePlaceRepository.existsByUserIdAndPlaceId(userId, placeId))
                .willReturn(false);

        favoritePlaceService.addFavoritePlace(userId, placeId);

        verify(favoritePlaceRepository).existsByUserIdAndPlaceId(userId, placeId);

        ArgumentCaptor<FavoritePlace> captor = ArgumentCaptor.forClass(FavoritePlace.class);
        verify(favoritePlaceRepository).save(captor.capture());

        FavoritePlace savedFavoritePlace = captor.getValue();
        assertEquals(userId, savedFavoritePlace.getUserId());
        assertEquals(placeId, savedFavoritePlace.getPlaceId());

    }

    @Test
    @DisplayName("이미 즐겨찾기한 장소는 중복 추가할 수 없다")
    void 이미_즐겨찾기한_장소는_중복_추가할_수_없다() {

        Long userId = 1L;
        Long placeId = 10L;

        given(favoritePlaceRepository.existsByUserIdAndPlaceId(userId, placeId))
                .willReturn(true);

        assertThrows(FavoritePlaceAlreadyExistsException.class,
                () -> favoritePlaceService.addFavoritePlace(userId, placeId));

        verify(favoritePlaceRepository).existsByUserIdAndPlaceId(userId, placeId);
        verify(favoritePlaceRepository, never()).save(any(FavoritePlace.class));
    }
}
