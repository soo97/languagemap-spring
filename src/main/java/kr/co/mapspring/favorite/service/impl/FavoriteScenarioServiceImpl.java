package kr.co.mapspring.favorite.service.impl;

import kr.co.mapspring.favorite.entity.FavoriteScenario;
import kr.co.mapspring.favorite.repository.FavoriteScenarioRepository;
import kr.co.mapspring.favorite.service.FavoriteScenarioService;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioAlreadyExistsException;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteScenarioServiceImpl implements FavoriteScenarioService {

    private final FavoriteScenarioRepository favoriteScenarioRepository;

    @Override
    @Transactional
    public void addFavoriteScenario(Long userId, Long scenarioId) {
        boolean alreadyExists =
                favoriteScenarioRepository.existsByUserIdAndScenarioId(userId, scenarioId);

        if (alreadyExists) {
            throw new FavoriteScenarioAlreadyExistsException();
        }

        FavoriteScenario favoriteScenario = FavoriteScenario.create(userId, scenarioId);
        favoriteScenarioRepository.save(favoriteScenario);
    }

    @Override
    @Transactional
    public void removeFavoriteScenario(Long userId, Long scenarioId) {
        FavoriteScenario favoriteScenario =
                favoriteScenarioRepository.findByUserIdAndScenarioId(userId, scenarioId)
                        .orElseThrow(FavoriteScenarioNotFoundException::new);

        favoriteScenarioRepository.delete(favoriteScenario);
    }

    @Override
    public List<FavoriteScenario> getFavoriteScenarios(Long userId) {
        return favoriteScenarioRepository.findAllByUserId(userId);
    }
}
