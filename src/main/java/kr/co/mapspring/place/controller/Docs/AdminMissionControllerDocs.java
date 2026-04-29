package kr.co.mapspring.place.controller.Docs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.place.dto.AdminCreateMissionDto;
import kr.co.mapspring.place.dto.AdminMissionListDto;
import kr.co.mapspring.place.dto.AdminReadMissionDto;
import kr.co.mapspring.place.dto.AdminUpdateMissionDto;

@Tag(name = "Admin Mission", description = "관리자 미션 CRUD API")
public interface AdminMissionControllerDocs {
	
	@Operation(
            summary = "미션 상세 조회",
            description = "missionId를 기준으로 미션 상세 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminReadMissionDto.ResponseRead.class))),
            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음")
    })
	ResponseEntity<ApiResponseDTO<AdminReadMissionDto.ResponseRead>> readMissionDetail(@PathVariable Long missionId);
	
	@Operation(
            summary = "미션 리스트 조회 및 검색",
            description = "keyword가 없으면 전체 조회, 있으면 미션명 검색"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminMissionListDto.ResponseList.class)))
    })
	ResponseEntity<ApiResponseDTO<List<AdminMissionListDto.ResponseList>>> readMissionList(@RequestParam(required = false) String keyword);
	
	@Operation(
            summary = "미션 수정",
            description = "missionId를 기준으로 미션을 수정한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음")
    })
	ResponseEntity<ApiResponseDTO<Void>> updateMission(@PathVariable Long missionId, AdminUpdateMissionDto.RequestUpdate request);
	
	 @Operation(
	            summary = "미션 생성",
	            description = "새로운 미션을 생성한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "생성 성공"),
	            @ApiResponse(responseCode = "400", description = "잘못된 요청 값")
	    })
	ResponseEntity<ApiResponseDTO<Void>> createMission(@RequestBody AdminCreateMissionDto.RequestCreate request);
	
	 @Operation(
	            summary = "미션 삭제",
	            description = "missionId를 기준으로 미션을 삭제한다."
	    )
	    @ApiResponses({
	            @ApiResponse(responseCode = "200", description = "삭제 성공"),
	            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음")
	    })
	ResponseEntity<ApiResponseDTO<Void>> deleteMission(@PathVariable Long missionId);
}
