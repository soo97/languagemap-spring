package kr.co.mapspring.learning.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.learning.dto.LearningGoalDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Learning Goal", description = "학습 목표 관련 API")
public interface LearningGoalControllerDocs {

    @Operation(
            summary = "학습 목표 선택",
            description = "사용자가 학습 목표를 선택합니다. 사용자는 최대 3개의 목표까지만 선택할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "목표 선택 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "목표 선택 성공",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 목표 선택 제한 초과",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 400,
                                              "message": "학습 목표는 최대 3개까지만 선택할 수 있습니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "목표 마스터를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "존재하지 않는 학습 목표입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 선택한 목표",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 409,
                                              "message": "이미 선택한 학습 목표입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> selectGoal(
            @RequestBody(
                    description = "학습 목표 선택 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LearningGoalDto.RequestSelectGoal.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "userId": 1,
                                              "goalMasterId": 10
                                            }
                                            """
                            )
                    )
            )
            LearningGoalDto.RequestSelectGoal request
    );

    @Operation(
            summary = "학습 목표 해제",
            description = "사용자가 선택한 학습 목표를 해제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "목표 해제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "목표 해제 성공",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 사용자 목표를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "사용자 목표를 찾을 수 없습니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> cancelGoal(
            @Parameter(description = "사용자 목표 ID", example = "1")
            Long userGoalId
    );

    @Operation(
            summary = "진행 중 학습 목표 조회",
            description = "사용자의 진행 중인 학습 목표 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "진행 중 목표 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "진행 중 학습 목표 조회 성공",
                                              "data": [
                                                {
                                                  "userGoalId": 1,
                                                  "goalMasterId": 10,
                                                  "goalTitle": "하루 1회 학습",
                                                  "goalType": "STUDY_COUNT",
                                                  "periodType": "DAILY",
                                                  "targetValue": 1,
                                                  "currentValue": 0,
                                                  "status": "ACTIVE",
                                                  "startDate": "2026-04-27",
                                                  "endDate": "2026-04-27"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseUserGoal>>> getActiveGoals(
            @Parameter(description = "사용자 ID", example = "1")
            Long userId
    );

    @Operation(
            summary = "완료된 학습 목표 조회",
            description = "사용자의 완료된 학습 목표 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "완료된 목표 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "완료된 학습 목표 조회 성공",
                                              "data": [
                                                {
                                                  "userGoalId": 2,
                                                  "goalMasterId": 10,
                                                  "goalTitle": "하루 1회 학습",
                                                  "goalType": "STUDY_COUNT",
                                                  "periodType": "DAILY",
                                                  "targetValue": 1,
                                                  "currentValue": 1,
                                                  "status": "COMPLETED",
                                                  "startDate": "2026-04-27",
                                                  "endDate": "2026-04-27"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseUserGoal>>> getCompletedGoals(
            @Parameter(description = "사용자 ID", example = "1")
            Long userId
    );

    @Operation(
            summary = "선택 가능한 학습 목표 조회",
            description = "사용자가 아직 선택하지 않은 학습 목표 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "선택 가능한 목표 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "선택 가능한 학습 목표 조회 성공",
                                              "data": [
                                                {
                                                  "goalMasterId": 11,
                                                  "goalTitle": "하루 3회 학습",
                                                  "goalType": "STUDY_COUNT",
                                                  "periodType": "DAILY",
                                                  "targetValue": 3
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseGoalMaster>>> getAvailableGoals(
            @Parameter(description = "사용자 ID", example = "1")
            Long userId
    );
}