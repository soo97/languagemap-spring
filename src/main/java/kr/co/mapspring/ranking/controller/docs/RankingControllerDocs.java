package kr.co.mapspring.ranking.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.ranking.dto.RankingDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Ranking", description = "랭킹 조회 API")
public interface RankingControllerDocs {

    @Operation(
            summary = "전체 랭킹 조회",
            description = "사용자별 총 점수를 기준으로 전체 랭킹을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "전체 랭킹 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getRankings();


    @Operation(
            summary = "내 랭킹 조회",
            description = "userId를 기준으로 자신의 랭킹을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "내 랭킹 조회 성공",
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
    ResponseEntity<ApiResponseDTO<RankingDto.ResponseRanking>> getMyRanking(Long userId);
}
