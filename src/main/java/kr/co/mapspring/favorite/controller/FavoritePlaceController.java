package kr.co.mapspring.favorite.controller;

import kr.co.mapspring.favorite.controller.docs.FavoritePlaceControllerDocs;
import kr.co.mapspring.favorite.dto.FavoritePlaceDto;
import kr.co.mapspring.favorite.service.FavoritePlaceService;
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
@RequestMapping("/api/favorites/places")
@RequiredArgsConstructor
public class FavoritePlaceController implements FavoritePlaceControllerDocs {

    private final FavoritePlaceService favoritePlaceService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> addFavoritePlace(@RequestBody FavoritePlaceDto.RequestAddFavoritePlace request) {
        favoritePlaceService.addFavoritePlace(request.getUserId(), request.getPlaceId());
        return ResponseEntity.ok(ApiResponseDTO.success("장소 즐겨찾기 추가 완료"));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<Void>> removeFavoritePlace(@RequestBody FavoritePlaceDto.RequestRemoveFavoritePlace request) {
        favoritePlaceService.removeFavoritePlace(request.getUserId(), request.getPlaceId());
        return ResponseEntity.ok(ApiResponseDTO.success("장소 즐겨찾기 삭제 완료"));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FavoritePlaceDto.ResponseFavoritePlace>>> getFavoritePlaces(@RequestParam Long userId) {

        List<FavoritePlaceDto.ResponseFavoritePlace> result =
                favoritePlaceService.getFavoritePlaces(userId)
                        .stream()
                        .map(FavoritePlaceDto.ResponseFavoritePlace::from)
                        .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("장소 즐겨찾기 조회 성공", result));
    }
}
