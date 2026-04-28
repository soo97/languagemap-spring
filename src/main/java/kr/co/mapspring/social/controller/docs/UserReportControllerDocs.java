package kr.co.mapspring.social.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.social.dto.UserReportDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "UserReport", description = "사용자 신고 API")
public interface UserReportControllerDocs {

    @Operation(
            summary = "사용자 신고",
            description = "특정 사용자를 신고합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "신고 접수 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "신고가 접수되었습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 400,
                                      "message": "자기 자신을 신고할 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> createReport(
            @RequestBody(
                    description = "사용자 신고 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserReportDto.RequestCreateReport.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "reporterId": 1,
                                      "reportedUserId": 2,
                                      "reason": "욕설 및 부적절한 행동"
                                    }
                                    """)
                    )
            )
            UserReportDto.RequestCreateReport request
    );

    @Operation(
            summary = "신고 이력 조회",
            description = "사용자가 신고한 이력을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "신고 이력 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "status": 200,
                                  "message": "신고 이력 조회 성공",
                                  "data": [
                                    {
                                      "reportId": 1,
                                      "reportedUserId": 2,
                                      "reason": "욕설",
                                      "status": "PENDING"
                                    }
                                  ]
                                }
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": false,
                                  "status": 400,
                                  "message": "userId는 필수입니다.",
                                  "data": null
                                }
                                """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<UserReportDto.ResponseReportHistory>>> getReportHistory(
            @RequestParam("userId") Long userId
    );
}
