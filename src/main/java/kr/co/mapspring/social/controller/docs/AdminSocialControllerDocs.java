package kr.co.mapspring.social.controller.docs;

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
import kr.co.mapspring.social.dto.AdminSocialDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Admin Social", description = "관리자 소셜 API")
public interface AdminSocialControllerDocs {

    @Operation(
            summary = "신고 목록 조회",
            description = "관리자가 전체 사용자 신고 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "신고 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "신고 목록 조회 성공",
                                      "data": [
                                        {
                                          "reportId": 1,
                                          "reporterId": 1,
                                          "reportedUserId": 2,
                                          "reason": "욕설",
                                          "status": "PENDING",
                                          "createdAt": "2026-04-29T10:30:00",
                                          "processedAt": null,
                                          "adminMemo": null
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminSocialDto.ResponseReport>>> getReports();


    @Operation(
            summary = "신고 상세 조회",
            description = "관리자가 특정 신고 내용을 상세 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "신고 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "신고 상세 조회 성공",
                                      "data": {
                                        "reportId": 1,
                                        "reporterId": 1,
                                        "reportedUserId": 2,
                                        "reason": "욕설",
                                        "status": "PENDING",
                                        "createdAt": "2026-04-29T10:30:00",
                                        "processedAt": null,
                                        "adminMemo": null
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "신고 내역을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "신고 내역을 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<AdminSocialDto.ResponseReport>> getReport(
            @Parameter(description = "신고 ID", example = "1")
            @PathVariable("reportId") Long reportId
    );


    @Operation(
            summary = "신고 상태 변경",
            description = "관리자가 신고 상태를 처리 완료 또는 반려 상태로 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "신고 상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "신고 상태 변경 성공",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "신고 내역을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "신고 내역을 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> updateReportStatus(
            @Parameter(description = "신고 ID", example = "1")
            @PathVariable("reportId") Long reportId,

            @RequestBody(
                    description = "신고 상태 변경 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminSocialDto.RequestUpdateReportStatus.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "status": "RESOLVED",
                                      "adminMemo": "확인 후 처리 완료"
                                    }
                                    """)
                    )
            )
            AdminSocialDto.RequestUpdateReportStatus request
    );


    @Operation(
            summary = "차단 이력 조회",
            description = "관리자가 전체 친구 관계 중 차단 상태의 이력을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "차단 이력 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "차단 이력 조회 성공",
                                      "data": [
                                        {
                                          "friendshipId": 1,
                                          "requesterId": 1,
                                          "addresseeId": 2,
                                          "status": "BLOCKED",
                                          "requestedAt": "2026-04-29T10:00:00",
                                          "respondedAt": "2026-04-29T10:30:00"
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminSocialDto.ResponseFriendshipHistory>>> getBlockedFriendships();


    @Operation(
            summary = "거절 이력 조회",
            description = "관리자가 전체 친구 관계 중 거절 상태의 이력을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "거절 이력 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "거절 이력 조회 성공",
                                      "data": [
                                        {
                                          "friendshipId": 1,
                                          "requesterId": 1,
                                          "addresseeId": 2,
                                          "status": "REJECTED",
                                          "requestedAt": "2026-04-29T10:00:00",
                                          "respondedAt": "2026-04-29T10:30:00"
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminSocialDto.ResponseFriendshipHistory>>> getRejectedFriendships();
}