package kr.co.mapspring.ranking.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.ranking.dto.AdminRankingDto;
import kr.co.mapspring.ranking.dto.RankingDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Admin Ranking", description = "관리자 랭킹 API")
public interface AdminRankingControllerDocs {

    @Operation(
            summary = "관리자 전체 랭킹 조회",
            description = "관리자가 전체 사용자의 누적 점수 기준 랭킹을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "관리자 전체 랭킹 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "관리자 전체 랭킹 조회 성공",
                                      "data": [
                                        {
                                          "rank": 1,
                                          "userId": 1,
                                          "totalScore": 300
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getRankings();

    @Operation(
            summary = "관리자 주간 랭킹 조회",
            description = "관리자가 최근 7일 기준 사용자의 점수 랭킹을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "관리자 주간 랭킹 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "관리자 주간 랭킹 조회 성공",
                                      "data": [
                                        {
                                          "rank": 1,
                                          "userId": 1,
                                          "totalScore": 150
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getWeeklyRankings();

    @Operation(
            summary = "전체 사용자 수 조회",
            description = "관리자가 전체 사용자 수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 사용자 수 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "전체 사용자 수 조회 성공",
                                      "data": {
                                        "totalUserCount": 10
                                      }
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<AdminRankingDto.ResponseTotalUserCount>> getTotalUserCount();
}