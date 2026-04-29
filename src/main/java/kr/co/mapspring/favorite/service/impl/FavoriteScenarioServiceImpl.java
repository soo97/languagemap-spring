package kr.co.mapspring.favorite.service.impl;

import kr.co.mapspring.favorite.entity.FavoriteScenario;
import kr.co.mapspring.favorite.repository.FavoriteScenarioRepository;
import kr.co.mapspring.favorite.service.FavoriteScenarioService;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioAlreadyExistsException;
import kr.co.mapspring.global.exception.favorite.FavoriteScenarioNotFoundException;
import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteScenarioServiceImpl implements FavoriteScenarioService {

    private final FavoriteScenarioRepository favoriteScenarioRepository;
    private final UserRepository userRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    @Transactional
    public void addFavoriteScenario(Long userId, Long scenarioId) {
        boolean alreadyExists =
                favoriteScenarioRepository.existsByUser_UserIdAndScenario_ScenarioId(userId, scenarioId);

        if (alreadyExists) {
            throw new FavoriteScenarioAlreadyExistsException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(ScenarioNotFoundException::new);

        FavoriteScenario favoriteScenario = FavoriteScenario.create(user, scenario);
        favoriteScenarioRepository.save(favoriteScenario);
    }

    @Override
    @Transactional
    public void removeFavoriteScenario(Long userId, Long scenarioId) {
        FavoriteScenario favoriteScenario =
                favoriteScenarioRepository.findByUser_UserIdAndScenario_ScenarioId(userId, scenarioId)
                        .orElseThrow(FavoriteScenarioNotFoundException::new);

        favoriteScenarioRepository.delete(favoriteScenario);
    }

    @Override
    public List<FavoriteScenario> getFavoriteScenarios(Long userId) {
        return favoriteScenarioRepository.findAllByUser_UserId(userId);
    }
}