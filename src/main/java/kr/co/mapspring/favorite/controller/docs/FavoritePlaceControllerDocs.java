package kr.co.mapspring.favorite.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            description = "사용자의 특정 장소를 즐겨찾기에 추가합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 추가 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "장소 즐겨찾기 추가 성공",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 즐겨찾기한 장소",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 409,
                                              "message": "이미 즐겨찾기한 장소입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 500,
                                              "message": "서버 내부 오류입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> addFavoritePlace(
            FavoritePlaceDto.RequestAddFavoritePlace request
    );

    @Operation(
            summary = "장소 즐겨찾기 삭제",
            description = "사용자의 장소 즐겨찾기를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "장소 즐겨찾기 삭제 성공",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "즐겨찾기 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "즐겨찾기한 장소를 찾을 수 없습니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 500,
                                              "message": "서버 내부 오류입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> removeFavoritePlace(
            FavoritePlaceDto.RequestRemoveFavoritePlace request
    );

    @Operation(
            summary = "장소 즐겨찾기 목록 조회",
            description = "사용자의 장소 즐겨찾기 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 즐겨찾기 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "장소 즐겨찾기 목록 조회 성공",
                                              "data": [
                                                {
                                                  "favoritePlaceId": 1,
                                                  "userId": 1,
                                                  "placeId": 10,
                                                  "createdAt": "2026-04-29T10:30:00"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청값",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 400,
                                              "message": "잘못된 요청입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 500,
                                              "message": "서버 내부 오류입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<FavoritePlaceDto.ResponseFavoritePlace>>> getFavoritePlaces(
            @Parameter(description = "사용자 ID", example = "1")
            Long userId
    );
}