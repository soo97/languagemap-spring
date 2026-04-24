package kr.co.mapspring.favorite.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.favorite.dto.FavoritePlaceDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Favorite Place", description = "장소 즐겨찾기 API")
public interface FavoritePlaceControllerDocs {

    @Operation(
            summary = "장소 즐겨찾기 추가",
            description = "사용자의 특정 장소를 즐겨찾기에 추가한다."
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "즐겨찾기 추가 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "409",
                    description = "이미 즐겨찾기한 장소",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> addFavoritePlace(FavoritePlaceDto.RequestAddFavoritePlace request);


    @Operation(
            summary = "장소 즐겨찾기 삭제",
            description = "사용자의 장소 즐겨찾기를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "즐겨찾기 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "즐겨찾기 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> removeFavoritePlace(FavoritePlaceDto.RequestRemoveFavoritePlace request);


    @Operation(
            summary = "장소 즐겨찾기 목록 조회",
            description = "사용자의 장소 즐겨찾기 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<List<FavoritePlaceDto.ResponseFavoritePlace>>> getFavoritePlaces(Long userId);
}
