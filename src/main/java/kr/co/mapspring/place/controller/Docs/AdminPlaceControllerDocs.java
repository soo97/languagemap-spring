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
import kr.co.mapspring.place.dto.AdminCreatePlaceDto;
import kr.co.mapspring.place.dto.AdminPlaceListDto;
import kr.co.mapspring.place.dto.AdminReadPlaceDto;
import kr.co.mapspring.place.dto.AdminUpdatePlaceDto;

@Tag(name = "Admin Place", description = "관리자 장소 CRUD API")
public interface AdminPlaceControllerDocs {
	
	 @Operation(
	            summary = "장소 생성",
	            description = "새로운 장소를 생성한다."
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
	            		description = "이미 존재하는 장소"
	            		)
	    })
	ResponseEntity<ApiResponseDTO<Void>> createPlace(@RequestBody AdminCreatePlaceDto.RequestCreate request);
	
	 @Operation(
	            summary = "장소 상세 조회",
	            description = "placeId를 기준으로 장소 상세 정보를 조회한다."
	    )
	    @ApiResponses({
	            @ApiResponse(
	            		responseCode = "200",
	            		description = "조회 성공",
	                    content = @Content(
	                    		schema = @Schema(implementation = AdminReadPlaceDto.ResponseRead.class)
	                    		)
	            		),
	            @ApiResponse(
	            		responseCode = "404",
	            		description = "장소를 찾을 수 없음"
	            		)
	    })
	ResponseEntity<ApiResponseDTO<AdminReadPlaceDto.ResponseRead>> readPlaceDetail(@PathVariable Long placeId);
	
	 @Operation(
	            summary = "장소 리스트 조회 및 검색",
	            description = "keyword가 없으면 전체 조회, 있으면 장소명 기준 검색"
	    )
	    @ApiResponses({
	            @ApiResponse(
	            		responseCode = "200", 
	            		description = "조회 성공",
	                    content = @Content(
	                    		schema = @Schema(implementation = AdminPlaceListDto.ResponseList.class)
	                    		)
	            		)
	    })
	ResponseEntity<ApiResponseDTO<List<AdminPlaceListDto.ResponseList>>> readPlaceList(@RequestParam(required = false) String keyword);
	 
	 @Operation(
	            summary = "장소 수정",
	            description = "placeId를 기준으로 장소 정보를 수정한다."
	    )
	    @ApiResponses({
	            @ApiResponse(
	            		responseCode = "200",
	            		description = "수정 성공"
	            		),
	            @ApiResponse(
	            		responseCode = "400",
	            		description = "잘못된 요청 값"),
	            @ApiResponse(
	            		responseCode = "404", 
	            		description = "장소를 찾을 수 없음"
	            		)
	    })
	ResponseEntity<ApiResponseDTO<Void>> updatePlace(@PathVariable Long placeId, @RequestBody AdminUpdatePlaceDto.RequestUpdate request);
	
	 @Operation(
	            summary = "장소 삭제",
	            description = "placeId를 기준으로 장소를 삭제한다."
	    )
	    @ApiResponses({
	            @ApiResponse(
	            		responseCode = "200",
	            		description = "삭제 성공"
	            		),
	            @ApiResponse(
	            		responseCode = "404",
	            		description = "장소를 찾을 수 없음"
	            		)
	    })
	ResponseEntity<ApiResponseDTO<Void>> deletePlace(@PathVariable Long placeId);
	
	

}
