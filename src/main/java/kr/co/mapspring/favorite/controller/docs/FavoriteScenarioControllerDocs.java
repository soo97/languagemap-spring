package kr.co.mapspring.favorite.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.favorite.dto.FavoriteScenarioDto;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Favorite Scenario", description = "시나리오 즐겨찾기 API")
public interface FavoriteScenarioControllerDocs {

    @Operation(
            summary = "시나리오 즐겨찾기 추가",
            description = "사용자가 특정 시나리오를 즐겨찾기에 추가합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "즐겨찾기 추가 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "이미 즐겨찾기한 시나리오",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> addFavoriteScenario(
            FavoriteScenarioDto.RequestAddFavoriteScenario request
    );

    @Operation(
            summary = "시나리오 즐겨찾기 삭제",
            description = "사용자가 등록한 시나리오 즐겨찾기를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "즐겨찾기 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "즐겨찾기 시나리오를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> removeFavoriteScenario(
            FavoriteScenarioDto.RequestRemoveFavoriteScenario request
    );

    @Operation(
            summary = "시나리오 즐겨찾기 목록 조회",
            description = "사용자의 시나리오 즐겨찾기 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "즐겨찾기 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청값",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<FavoriteScenarioDto.ResponseFavoriteScenario>>> getFavoriteScenarios(
            Long userId
    );
}
