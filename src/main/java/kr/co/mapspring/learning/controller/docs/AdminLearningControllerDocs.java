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
import kr.co.mapspring.learning.dto.AdminLearningDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Admin Learning", description = "관리자 학습 API")
public interface AdminLearningControllerDocs {

    @Operation(
            summary = "전체 학습 기록 조회",
            description = "관리자가 전체 사용자의 학습 기록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 학습 기록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "전체 학습 기록 조회 성공",
                                      "data": [
                                        {
                                          "studyLogId": 1,
                                          "userId": 1,
                                          "sessionId": 1,
                                          "studyType": "SCENARIO",
                                          "earnedExp": 20,
                                          "naturalnessScore": 80,
                                          "fluencyScore": 70,
                                          "totalScore": 75
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminLearningDto.ResponseStudyLog>>> getStudyLogs();

    @Operation(
            summary = "사용자별 학습 기록 조회",
            description = "관리자가 특정 사용자의 학습 기록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 학습 기록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "사용자 학습 기록 조회 성공",
                                      "data": [
                                        {
                                          "studyLogId": 1,
                                          "userId": 1,
                                          "sessionId": 1,
                                          "studyType": "SPEAKING",
                                          "earnedExp": 30,
                                          "naturalnessScore": 90,
                                          "fluencyScore": 80,
                                          "totalScore": 85
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminLearningDto.ResponseStudyLog>>> getStudyLogsByUserId(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable("userId") Long userId
    );

    @Operation(
            summary = "학습 목표 목록 조회",
            description = "관리자가 전체 학습 목표 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "학습 목표 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "학습 목표 목록 조회 성공",
                                      "data": [
                                        {
                                          "goalMasterId": 1,
                                          "goalTitle": "하루 3회 학습",
                                          "goalDescription": "하루에 학습을 3회 완료합니다.",
                                          "goalType": "STUDY_COUNT",
                                          "periodType": "DAILY",
                                          "targetValue": 3,
                                          "active": true
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminLearningDto.ResponseGoalMaster>>> getGoalMasters();

    @Operation(
            summary = "학습 목표 활성 상태 변경",
            description = "관리자가 학습 목표의 활성화 여부를 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "학습 목표 활성 상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "학습 목표 활성 상태 변경 성공",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> updateGoalActive(
            @Parameter(description = "목표 마스터 ID", example = "1")
            @PathVariable("goalMasterId") Long goalMasterId,

            @RequestBody(
                    description = "학습 목표 활성 상태 변경 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminLearningDto.RequestUpdateGoalActive.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "active": false
                                    }
                                    """)
                    )
            )
            AdminLearningDto.RequestUpdateGoalActive request
    );

    @Operation(
            summary = "학습 목표 생성",
            description = "관리자가 새로운 학습 목표를 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "학습 목표 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "학습 목표 생성 성공",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> createGoal(
            @RequestBody(
                    description = "학습 목표 생성 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminLearningDto.RequestCreateGoal.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "badgeId": 1,
                                      "goalType": "STUDY_COUNT",
                                      "goalTitle": "하루 학습 3회",
                                      "goalDescription": "하루에 학습을 3회 완료합니다.",
                                      "targetValue": 3,
                                      "periodType": "DAILY"
                                    }
                                    """)
                    )
            )
            AdminLearningDto.RequestCreateGoal request
    );

    @Operation(
            summary = "학습 목표 수정",
            description = "관리자가 기존 학습 목표 정보를 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "학습 목표 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "학습 목표 수정 성공",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "목표 마스터를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "존재하지 않는 학습 목표입니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> updateGoal(
            @Parameter(description = "목표 마스터 ID", example = "1")
            @PathVariable("goalMasterId") Long goalMasterId,

            @RequestBody(
                    description = "학습 목표 수정 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminLearningDto.RequestUpdateGoal.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "badgeId": 1,
                                      "goalType": "STUDY_TIME",
                                      "goalTitle": "하루 학습 30분",
                                      "goalDescription": "하루에 30분 이상 학습합니다.",
                                      "targetValue": 30,
                                      "periodType": "DAILY"
                                    }
                                    """)
                    )
            )
            AdminLearningDto.RequestUpdateGoal request
    );

    @Operation(
            summary = "학습 목표 삭제",
            description = "관리자가 학습 목표를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "학습 목표 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "학습 목표 삭제 성공",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "목표 마스터를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "존재하지 않는 학습 목표입니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> deleteGoal(
            @Parameter(description = "목표 마스터 ID", example = "1")
            @PathVariable("goalMasterId") Long goalMasterId
    );
}