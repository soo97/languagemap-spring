package kr.co.mapspring.favorite.controller;

import kr.co.mapspring.favorite.controller.docs.FavoriteScenarioControllerDocs;
import kr.co.mapspring.favorite.dto.FavoriteScenarioDto;
import kr.co.mapspring.favorite.entity.FavoriteScenario;
import kr.co.mapspring.favorite.service.FavoriteScenarioService;
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
@RequiredArgsConstructor
@RequestMapping("api/favorites/scenarios")
public class FavoriteScenarioController implements FavoriteScenarioControllerDocs {

    private final FavoriteScenarioService favoriteScenarioService;

    @Override
    @PostMapping
    public void addFavoriteScenario(@RequestBody FavoriteScenarioDto.RequestAddFavoriteScenario request) {
        favoriteScenarioService.addFavoriteScenario(
                request.getUserId(),
                request.getScenarioId()
        );
    }

    @Override
    @DeleteMapping
    public void removeFavoriteScenario(@RequestBody FavoriteScenarioDto.RequestRemoveFavoriteScenario request) {
        favoriteScenarioService.removeFavoriteScenario(
                request.getUserId(),
                request.getScenarioId()
        );
    }

    @Override
    @GetMapping
    public List<FavoriteScenarioDto.ResponseFavoriteScenario> getFavoriteScenarios(@RequestParam Long userId) {
        List<FavoriteScenario> favoriteScenarios = favoriteScenarioService.getFavoriteScenarios(userId);

        return favoriteScenarios.stream()
                .map(FavoriteScenarioDto.ResponseFavoriteScenario::from)
                .toList();
    }

}
