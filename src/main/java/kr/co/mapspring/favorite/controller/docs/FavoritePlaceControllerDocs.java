package kr.co.mapspring.favorite.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.favorite.dto.FavoritePlaceDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Favorite Place", description = "장소 즐겨찾기 API")
public interface FavoritePlaceControllerDocs {

    @Operation(summary = "장소 즐겨찾기 추가", description = "사용자의 특정 장소를 즐겨찾기에 추가한다.")
    void addFavoritePlace(@RequestBody FavoritePlaceDto.RequestAddFavoritePlace request);

    @Operation(summary = "장소 즐겨찾기 삭제", description = "사용자의 장소 즐겨찾기를 삭제한다.")
    void removeFavoritePlace(@RequestBody FavoritePlaceDto.RequestRemoveFavoritePlace request);

    @Operation(summary = "장소 즐겨찾기 목록 조회", description = "사용자의 장소 즐겨찾기 목록을 조회한다.")
    List<FavoritePlaceDto.ResponseFavoritePlace> getFavoritePlaces(@RequestParam Long userId);
}
