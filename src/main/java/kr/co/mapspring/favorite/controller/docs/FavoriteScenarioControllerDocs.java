package kr.co.mapspring.favorite.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.favorite.dto.FavoriteScenarioDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Favorite Scenario", description = "시나리오 즐겨찾기 API")
public interface FavoriteScenarioControllerDocs {

    @Operation(summary = "시나리오 즐겨찾기 추가", description = "사용자의 특정 시나리오를 즐겨찾기에 추가한다.")
    void addFavoriteScenario(@RequestBody FavoriteScenarioDto.RequestAddFavoriteScenario request);

    @Operation(summary = "시나리오 즐겨찾기 삭제", description = "사용자의 시나리오 즐겨찾기를 삭제한다.")
    void removeFavoriteScenario(@RequestBody FavoriteScenarioDto.RequestRemoveFavoriteScenario request);

    @Operation(summary = "시나리오 즐겨찾기 목록 조회", description = "사용자의 시나리오 즐겨찾기 목록을 조회한다.")
    List<FavoriteScenarioDto.ResponseFavoriteScenario> getFavoriteScenarios(@RequestParam Long userId);

}
