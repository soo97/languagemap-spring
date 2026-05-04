package kr.co.mapspring.place.controller.Docs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.place.dto.AdminCreateRegionDto;
import kr.co.mapspring.place.dto.AdminReadRegionDto;
import kr.co.mapspring.place.dto.AdminRegionListDto;
import kr.co.mapspring.place.dto.AdminUpdateRegionDto;

@Tag(name = "Admin Region", description = "관리자 지역 CRUD API")
public interface AdminRegionControllerDocs {

    // 생성
    @Operation(
            summary = "지역 생성",
            description = "새로운 지역을 생성한다."
    )
    @ApiResponses({
            @ApiResponse(
            		responseCode = "200",
            		description = "생성 성공"
            		),
            @ApiResponse(
            		responseCode = "400",
            		description = "잘못된 요청 값"
            		),
            @ApiResponse(
            		responseCode = "409",
            		description = "이미 존재하는 지역"
            		)
    })
    ResponseEntity<ApiResponseDTO<Void>> createRegion(@RequestBody AdminCreateRegionDto.RequestCreate request);

    // 상세 조회
    @Operation(
            summary = "지역 상세 조회",
            description = "regionId를 기준으로 지역 상세 정보를 조회한다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                    		schema = @Schema(implementation = AdminReadRegionDto.ResponseRead.class)
                    		)
            ),
            @ApiResponse(
            		responseCode = "404", 
            		description = "지역을 찾을 수 없음"
            		)
    })
    ResponseEntity<ApiResponseDTO<AdminReadRegionDto.ResponseRead>> readRegionDetail(@PathVariable Long regionId);

    // 리스트
    @Operation(
            summary = "지역 리스트 조회 및 검색",
            description = "keyword가 없으면 전체 조회, 있으면 도시명 기준 검색"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                    		schema = @Schema(implementation = AdminRegionListDto.ResponseList.class)
                    		)
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminRegionListDto.ResponseList>>> readRegionList(@RequestParam(required = false) String keyword);

    // 수정
    @Operation(
            summary = "지역 수정",
            description = "regionId를 기준으로 지역 정보를 수정한다."
    )
    @ApiResponses({
            @ApiResponse(
            		responseCode = "200", 
            		description = "수정 성공"
            		),
            @ApiResponse(
            		responseCode = "400", 
            		description = "잘못된 요청 값"
            		),
            @ApiResponse(
            		responseCode = "404",
            		description = "지역을 찾을 수 없음"
            		)
    })
    ResponseEntity<ApiResponseDTO<Void>> updateRegion(@PathVariable Long regionId, @RequestBody AdminUpdateRegionDto.RequestUpdate request);

    // 삭제
    @Operation(
            summary = "지역 삭제",
            description = "regionId를 기준으로 지역을 삭제한다."
    )
    @ApiResponses({
            @ApiResponse(
            		responseCode = "200", 
            		description = "삭제 성공"
            		),
            @ApiResponse(
            		responseCode = "404",
            		description = "지역을 찾을 수 없음"
            		)
    })
    ResponseEntity<ApiResponseDTO<Void>> deleteRegion(@PathVariable Long regionId);
}