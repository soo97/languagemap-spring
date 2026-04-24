package kr.co.mapspring.favorite.controller;

import kr.co.mapspring.favorite.controller.docs.FavoriteScenarioControllerDocs;
import kr.co.mapspring.favorite.dto.FavoriteScenarioDto;
import kr.co.mapspring.favorite.entity.FavoriteScenario;
import kr.co.mapspring.favorite.service.FavoriteScenarioService;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/favorites/scenarios")
public class FavoriteScenarioController implements FavoriteScenarioControllerDocs {

    private final FavoriteScenarioService favoriteScenarioService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> addFavoriteScenario(@RequestBody FavoriteScenarioDto.RequestAddFavoriteScenario request) {
        favoriteScenarioService.addFavoriteScenario(request.getUserId(), request.getScenarioId());
        return ResponseEntity.ok(ApiResponseDTO.success("시나리오 즐겨찾기 추가 완료"));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<Void>> removeFavoriteScenario(@RequestBody FavoriteScenarioDto.RequestRemoveFavoriteScenario request) {
        favoriteScenarioService.removeFavoriteScenario(request.getUserId(), request.getScenarioId());
        return ResponseEntity.ok(ApiResponseDTO.success("시나리오 즐겨찾기 삭제 완료"));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FavoriteScenarioDto.ResponseFavoriteScenario>>> getFavoriteScenarios(@RequestParam Long userId) {
        List<FavoriteScenarioDto.ResponseFavoriteScenario> result =
                favoriteScenarioService.getFavoriteScenarios(userId)
                        .stream()
                        .map(FavoriteScenarioDto.ResponseFavoriteScenario::from)
                        .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("시나리오 즐겨찾기 조회 성공", result));
    }

}
