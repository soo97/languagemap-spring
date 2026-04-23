package kr.co.mapspring.favorite.service;

import kr.co.mapspring.favorite.entity.FavoriteScenario;
import kr.co.mapspring.favorite.repository.FavoriteScenarioRepository;
import kr.co.mapspring.favorite.service.impl.FavoriteScenarioServiceImpl;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioAlreadyExistsException;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteScenarioServiceTest {

    @Mock
    private FavoriteScenarioRepository favoriteScenarioRepository;

    @InjectMocks
    private FavoriteScenarioServiceImpl favoriteScenarioService;

    @Test
    @DisplayName("즐겨찾기 시나리오를 정상적으로 추가한다")
    void 즐겨찾기_시나리오를_정상적으로_추가한다() {

        Long userId = 1L;
        Long scenarioId = 10L;

        given(favoriteScenarioRepository.existsByUserIdAndScenarioId(userId, scenarioId))
                .willReturn(false);

        favoriteScenarioService.addFavoriteScenario(userId, scenarioId);

        verify(favoriteScenarioRepository).existsByUserIdAndScenarioId(userId, scenarioId);

        ArgumentCaptor<FavoriteScenario> captor = ArgumentCaptor.forClass(FavoriteScenario.class);
        verify(favoriteScenarioRepository).save(captor.capture());

        FavoriteScenario savedFavoriteScenario = captor.getValue();
        assertEquals(userId, savedFavoriteScenario.getUserId());
        assertEquals(scenarioId, savedFavoriteScenario.getScenarioId());

    }

    @Test
    @DisplayName("이미 즐겨찾기한 시나리오는 중복 추가할 수 없다")
    void 이미_즐겨찾기한_시나리오는_중복_추가할_수_없다() {

        Long userId = 1L;
        Long scenarioId = 10L;

        given(favoriteScenarioRepository.existsByUserIdAndScenarioId(userId, scenarioId))
                .willReturn(true);

        assertThrows(FavoriteScenarioAlreadyExistsException.class,
                () -> favoriteScenarioService.addFavoriteScenario(userId, scenarioId));

        verify(favoriteScenarioRepository).existsByUserIdAndScenarioId(userId, scenarioId);
        verify(favoriteScenarioRepository, never()).save(any(FavoriteScenario.class));
    }

    @Test
    @DisplayName("즐겨찾기 시나리오를 정상적으로 삭제한다")
    void 즐겨찾기_시나리오를_정상적으로_삭제한다() {

        Long userId = 1L;
        Long scenarioId = 10L;
        FavoriteScenario favoriteScenario = FavoriteScenario.of(1L, userId, scenarioId);

        given(favoriteScenarioRepository.findByUserIdAndScenarioId(userId, scenarioId))
                .willReturn(Optional.of(favoriteScenario));

        favoriteScenarioService.removeFavoriteScenario(userId, scenarioId);

        verify(favoriteScenarioRepository).findByUserIdAndScenarioId(userId, scenarioId);
        verify(favoriteScenarioRepository).delete(favoriteScenario);
    }

    @Test
    @DisplayName("존재하지 않는 즐겨찾기 시나리오는 삭제할 수 없다")
    void 존재하지_않는_즐겨찾기_시나리오는_삭제할_수_없다() {

        Long userId = 1L;
        Long scenarioId = 10L;

        given(favoriteScenarioRepository.findByUserIdAndScenarioId(userId, scenarioId))
                .willReturn(Optional.empty());

        assertThrows(FavoriteScenarioNotFoundException.class,
                () -> favoriteScenarioService.removeFavoriteScenario(userId, scenarioId));

        verify(favoriteScenarioRepository).findByUserIdAndScenarioId(userId, scenarioId);
        verify(favoriteScenarioRepository, never()).delete(any(FavoriteScenario.class));
    }

    @Test
    @DisplayName("사용자의 즐겨찾기 시나리오 목록을 조회한다")
    void 사용자의_즐겨찾기_장소_목록을_조회한다() {

        Long userId = 1L;

        FavoriteScenario favoriteScenario1 = FavoriteScenario.of(1L, userId, 10L);
        FavoriteScenario favoriteScenario2 = FavoriteScenario.of(2L, userId, 20L);

        given(favoriteScenarioRepository.findAllByUserId(userId))
                .willReturn(List.of(favoriteScenario1, favoriteScenario2));

        List<FavoriteScenario> result = favoriteScenarioService.getFavoriteScenarios(userId);

        verify(favoriteScenarioRepository).findAllByUserId(userId);
        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getScenarioId());
        assertEquals(20L, result.get(1).getScenarioId());


    }
}
