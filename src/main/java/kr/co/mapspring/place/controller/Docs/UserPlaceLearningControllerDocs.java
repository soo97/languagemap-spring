package kr.co.mapspring.place.controller.Docs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.place.dto.UserChatDto;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserLearningProgressDto;
import kr.co.mapspring.place.dto.UserMissionCompleteDto;
import kr.co.mapspring.place.dto.UserMissionStartDto;
import kr.co.mapspring.place.dto.UserPlaceListDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.dto.UserRegionListDto;
import kr.co.mapspring.place.dto.UserMissionStartDto.RequsetMissionStart;
import kr.co.mapspring.user.entity.User;

@Tag(name = "User Place Learning", description = "사용자 장소 학습 및 세션 API")
public interface UserPlaceLearningControllerDocs {
	
	 @Operation(
	    		summary = "마커 리스트 조회",
	    		description = "placeId, 위도, 경도를 기준으로 구글 맵에 마커를 찍는다."
	    		)
	    @ApiResponses({
	            @ApiResponse(
	            		responseCode = "200",
	            		description = "조회 성공",
	            		content = @Content(
	                            schema = @Schema(implementation = UserReadPlaceDto.ResponseRead.class)
	                    )
	),
	            @ApiResponse(
	                    responseCode = "404",
	                    description = "장소를 찾을 수 없음"
	            )
	    })
	ResponseEntity<ApiResponseDTO<List<UserPlaceListDto.ResponseList>>> readPlaceMarker(
			@AuthenticationPrincipal User user);

    @Operation(
    		summary = "마커 상세 정보 조회",
    		description = "placeId를 기준으로 장소 정보와 시나리오 및 미션 리스트를 조회한다."
    		)
    @ApiResponses({
            @ApiResponse(
            		responseCode = "200",
            		description = "조회 성공",
            		content = @Content(
                            schema = @Schema(implementation = UserReadPlaceDto.ResponseRead.class)
                    )
)
    })
    ResponseEntity<ApiResponseDTO<UserReadPlaceDto.ResponseRead>> clickPlace(
            @PathVariable Long placeId
    );


    @Operation(
            summary = "학습, 미션 세션 생성",
            description = "placeId를 기준으로 학습, 미션 세션을 생성하고 sessionId를 반환한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "학습, 미션 세션 생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserCreateLearningSessionDto.ResponseCreate.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 값"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 또는 장소를 찾을 수 없음"
            )
    })
    ResponseEntity<ApiResponseDTO<UserCreateLearningSessionDto.ResponseCreate>> learningStart(
            @PathVariable Long placeId,
            @RequestBody UserCreateLearningSessionDto.RequestCreate request
    );
    
    @Operation(
            summary = "미션 시작",
            description = "sessionId와 missionId를 기준으로 특정 학습 세션의 미션을 시작 상태로 변경하고, AI 첫 메시지를 반환한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 시작 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserMissionStartDto.ResponseMissionStart.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "미션 세션 또는 미션을 찾을 수 없음"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 진행 중이거나 완료된 미션"
            )
    })
    public ResponseEntity<ApiResponseDTO<UserMissionStartDto.ResponseMissionStart>> missionStart(
	        @PathVariable("sessionId") Long sessionId,
	        @PathVariable("missionId") Long missionId,
	        @RequestBody RequsetMissionStart request
	        );
    
    @Operation(
            summary = "AI 채팅",
            description = "sessionId를 기준으로 현재 RUNNING 상태의 미션 세션을 찾고, 사용자 메시지를 저장한 뒤 AI 응답을 반환한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "AI 채팅 응답 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserChatDto.ResponseChat.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "진행 중인 미션 세션을 찾을 수 없음"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 값"
            )
    })
    ResponseEntity<ApiResponseDTO<UserChatDto.ResponseChat>> chat(@RequestBody UserChatDto.RequestChat request);
    
    @Operation(
            summary = "미션 완료",
            description = """
                    현재 진행 중인 미션을 완료 처리합니다.
                    
                    - MissionSession 상태를 COMPLETED로 변경합니다.
                    - 모든 MissionSession이 COMPLETED 상태가 되면 LearningSession도 COMPLETED로 변경합니다.
                    - 마지막 미션 완료 시 세션 평가(SessionEvaluation)를 생성합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 완료 처리 성공",
                    content = @Content(
                            schema = @Schema(
                                    implementation = UserMissionCompleteDto.ResponseComplete.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 미션 세션을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponseDTO.class)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<UserMissionCompleteDto.ResponseComplete>> missionComplete(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("missionId") Long missionId,
            @RequestBody UserMissionCompleteDto.RequestComplete request
    );
    
    @Operation(
            summary = "내 학습 진행 상황 조회",
            description = """
                    JWT 토큰에 포함된 로그인 사용자 정보를 기준으로
                    사용자의 장소별 학습 진행 상황을 조회합니다.

                    - 진행 중인 학습 세션
                    - 진행 중인 미션 ID
                    - 완료한 미션 ID 목록
                    - 저장된 대화 메시지 목록
                    - 최종 평가 메시지
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 학습 진행 상황 조회 성공",
                    content = @Content(
                            schema = @Schema(
                                    implementation = UserLearningProgressDto.Response.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    ResponseEntity<ApiResponseDTO<List<UserLearningProgressDto.Response>>> readMyProgress(
	        @AuthenticationPrincipal User user
	);
    
    ResponseEntity<ApiResponseDTO<List<UserRegionListDto.ResponseList>>> readUserRegionList();
}