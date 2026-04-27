package kr.co.mapspring.learning.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.learning.dto.LearningGoalDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LearningGoalControllerDocs {

    @Operation(
            summary = "학습 목표 선택",
            description = "사용자가 학습 목표를 선택합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "목표 선택 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "잘못된 요청 또는 목표 선택 제한 초과",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "409",
                    description = "이미 선택한 목표",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> selectGoal(LearningGoalDto.RequestSelectGoal request);


    @Operation(
            summary = "학습 목표 해제",
            description = "사용자가 선택한 학습 목표를 해제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "목표 해제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "해당 목표를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> cancelGoal(Long userGoalId);


    @Operation(
            summary = "진행 중 학습 목표 조회",
            description = "사용자의 진행 중인 학습 목표 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseUserGoal>>> getActiveGoals(Long userId);


    @Operation(
            summary = "완료된 학습 목표 조회",
            description = "사용자의 완료된 학습 목표 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseUserGoal>>> getCompletedGoals(Long userId);


    @Operation(
            summary = "선택 가능한 학습 목표 조회",
            description = "사용자가 선택할 수 있는 학습 목표 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            )
    })
    ResponseEntity<ApiResponseDTO<List<LearningGoalDto.ResponseGoalMaster>>> getAvailableGoals(Long userId);
}
