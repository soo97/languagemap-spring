package kr.co.mapspring.ranking.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.ranking.dto.RankingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Ranking", description = "랭킹 조회 API")
public interface RankingControllerDocs {

    @Operation(
            summary = "전체 랭킹 조회",
            description = "사용자별 총 점수를 기준으로 전체 랭킹을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 랭킹 조회 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getRankings();

    @Operation(
            summary = "내 랭킹 조회",
            description = "userId를 기준으로 자신의 랭킹을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 랭킹 조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "랭킹을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "랭킹을 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<RankingDto.ResponseRanking>> getMyRanking(
            @Parameter(description = "사용자 ID", example = "1", required = true)
            @RequestParam("userId") Long userId
    );

    @Operation(
            summary = "친구 랭킹 조회",
            description = "userId를 기준으로 친구들의 랭킹을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 랭킹 조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "랭킹을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "랭킹을 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getFriendRankings(
            @Parameter(description = "사용자 ID", example = "1", required = true)
            @RequestParam("userId") Long userId
    );

    @Operation(
            summary = "친구 최고 점수 조회",
            description = "userId를 기준으로 친구들 중 가장 높은 점수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 최고 점수 조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "점수를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "점수를 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Long>> getFriendBestScore(
            @Parameter(description = "사용자 ID", example = "1", required = true)
            @RequestParam("userId") Long userId
    );

    @Operation(
            summary = "친구 평균 점수 조회",
            description = "userId를 기준으로 친구들의 평균 점수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 평균 점수 조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "점수를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "점수를 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Double>> getFriendAverageScore(
            @Parameter(description = "사용자 ID", example = "1", required = true)
            @RequestParam("userId") Long userId
    );

    @Operation(
            summary = "주간 랭킹 조회",
            description = "최근 7일 동안의 학습 점수를 기준으로 주간 랭킹을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주간 랭킹 조회 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getWeeklyRankings();

    @Operation(
            summary = "전체 사용자 수 조회",
            description = "서비스에 가입한 전체 사용자 수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 사용자 수 조회 성공"),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 500,
                                      "message": "서버 내부 오류가 발생했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Long>> getTotalUserCount();
}