package kr.co.mapspring.favorite.service;

import kr.co.mapspring.favorite.entity.FavoriteScenario;
import kr.co.mapspring.favorite.repository.FavoriteScenarioRepository;
import kr.co.mapspring.favorite.service.impl.FavoriteScenarioServiceImpl;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioAlreadyExistsException;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioNotFoundException;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FavoriteScenarioServiceTest {

    @Mock
    private FavoriteScenarioRepository favoriteScenarioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScenarioRepository scenarioRepository;

    @InjectMocks
    private FavoriteScenarioServiceImpl favoriteScenarioService;

    @Test
    @DisplayName("즐겨찾기 시나리오를 정상적으로 추가한다")
    void 즐겨찾기_시나리오를_정상적으로_추가한다() {
        Long userId = 1L;
        Long scenarioId = 10L;

        User user = mock(User.class);
        Scenario scenario = mock(Scenario.class);

        given(user.getUserId()).willReturn(userId);
        given(scenario.getScenarioId()).willReturn(scenarioId);

        given(favoriteScenarioRepository.existsByUser_UserIdAndScenario_ScenarioId(userId, scenarioId))
                .willReturn(false);
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(scenarioRepository.findById(scenarioId))
                .willReturn(Optional.of(scenario));

        favoriteScenarioService.addFavoriteScenario(userId, scenarioId);

        verify(favoriteScenarioRepository).existsByUser_UserIdAndScenario_ScenarioId(userId, scenarioId);
        verify(userRepository).findById(userId);
        verify(scenarioRepository).findById(scenarioId);

        ArgumentCaptor<FavoriteScenario> captor = ArgumentCaptor.forClass(FavoriteScenario.class);
        verify(favoriteScenarioRepository).save(captor.capture());

        FavoriteScenario savedFavoriteScenario = captor.getValue();

        assertEquals(userId, savedFavoriteScenario.getUser().getUserId());
        assertEquals(scenarioId, savedFavoriteScenario.getScenario().getScenarioId());
    }

    @Test
    @DisplayName("이미 즐겨찾기한 시나리오는 중복 추가할 수 없다")
    void 이미_즐겨찾기한_시나리오는_중복_추가할_수_없다() {
        Long userId = 1L;
        Long scenarioId = 10L;

        given(favoriteScenarioRepository.existsByUser_UserIdAndScenario_ScenarioId(userId, scenarioId))
                .willReturn(true);

        assertThrows(FavoriteScenarioAlreadyExistsException.class,
                () -> favoriteScenarioService.addFavoriteScenario(userId, scenarioId));

        verify(favoriteScenarioRepository).existsByUser_UserIdAndScenario_ScenarioId(userId, scenarioId);
        verify(userRepository, never()).findById(any());
        verify(scenarioRepository, never()).findById(any());
        verify(favoriteScenarioRepository, never()).save(any(FavoriteScenario.class));
    }

    @Test
    @DisplayName("즐겨찾기 시나리오를 정상적으로 삭제한다")
    void 즐겨찾기_시나리오를_정상적으로_삭제한다() {
        Long userId = 1L;
        Long scenarioId = 10L;

        User user = mock(User.class);
        Scenario scenario = mock(Scenario.class);

        FavoriteScenario favoriteScenario = FavoriteScenario.of(1L, user, scenario);

        given(favoriteScenarioRepository.findByUser_UserIdAndScenario_ScenarioId(userId, scenarioId))
                .willReturn(Optional.of(favoriteScenario));

        favoriteScenarioService.removeFavoriteScenario(userId, scenarioId);

        verify(favoriteScenarioRepository).findByUser_UserIdAndScenario_ScenarioId(userId, scenarioId);
        verify(favoriteScenarioRepository).delete(favoriteScenario);
    }

    @Test
    @DisplayName("존재하지 않는 즐겨찾기 시나리오는 삭제할 수 없다")
    void 존재하지_않는_즐겨찾기_시나리오는_삭제할_수_없다() {
        Long userId = 1L;
        Long scenarioId = 10L;

        given(favoriteScenarioRepository.findByUser_UserIdAndScenario_ScenarioId(userId, scenarioId))
                .willReturn(Optional.empty());

        assertThrows(FavoriteScenarioNotFoundException.class,
                () -> favoriteScenarioService.removeFavoriteScenario(userId, scenarioId));

        verify(favoriteScenarioRepository).findByUser_UserIdAndScenario_ScenarioId(userId, scenarioId);
        verify(favoriteScenarioRepository, never()).delete(any(FavoriteScenario.class));
    }

    @Test
    @DisplayName("사용자의 즐겨찾기 시나리오 목록을 조회한다")
    void 사용자의_즐겨찾기_시나리오_목록을_조회한다() {
        Long userId = 1L;

        User user = mock(User.class);

        Scenario scenario1 = mock(Scenario.class);
        Scenario scenario2 = mock(Scenario.class);

        given(scenario1.getScenarioId()).willReturn(10L);
        given(scenario2.getScenarioId()).willReturn(20L);

        FavoriteScenario favoriteScenario1 = FavoriteScenario.of(1L, user, scenario1);
        FavoriteScenario favoriteScenario2 = FavoriteScenario.of(2L, user, scenario2);

        given(favoriteScenarioRepository.findAllByUser_UserId(userId))
                .willReturn(List.of(favoriteScenario1, favoriteScenario2));

        List<FavoriteScenario> result = favoriteScenarioService.getFavoriteScenarios(userId);

        verify(favoriteScenarioRepository).findAllByUser_UserId(userId);

        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getScenario().getScenarioId());
        assertEquals(20L, result.get(1).getScenario().getScenarioId());
    }
}