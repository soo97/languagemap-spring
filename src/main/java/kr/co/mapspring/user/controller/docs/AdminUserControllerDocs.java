package kr.co.mapspring.user.controller.docs;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.dto.AdminUserDto;

@Tag(name = "Admin-User", description = "관리자 회원 관리 API")
public interface AdminUserControllerDocs {

    @Operation(summary = "회원 목록 조회", description = "전체 회원 목록을 조회합니다. keyword로 이름/이메일 검색 가능합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "회원 목록 조회 완료",
                                              "data": [
                                                {
                                                  "userId": 1,
                                                  "email": "test@naver.com",
                                                  "name": "홍길동",
                                                  "phoneNumber": "01012345678",
                                                  "role": "USER",
                                                  "status": "ACTIVE",
                                                  "createdAt": "2024-01-01T00:00:00"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AdminUserDto.ResponseList>>> getUserList(
            @RequestParam(name = "keyword", required = false) String keyword
    );

    @Operation(summary = "회원 상세 조회", description = "특정 회원의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "회원 상세 조회 완료",
                                              "data": {
                                                "userId": 1,
                                                "email": "test@naver.com",
                                                "name": "홍길동",
                                                "birthDate": "2000-01-01",
                                                "address": "서울시 강남구",
                                                "phoneNumber": "01012345678",
                                                "role": "USER",
                                                "status": "ACTIVE",
                                                "lastLoginAt": "2024-01-01T00:00:00",
                                                "createdAt": "2024-01-01T00:00:00",
                                                "updatedAt": "2024-01-01T00:00:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 유저",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "존재하지 않는 이메일입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<AdminUserDto.ResponseDetail>> getUserDetail(
            @PathVariable("userId") Long userId
    );

    @Operation(summary = "회원 상태 변경", description = "특정 회원의 상태를 변경합니다. (ACTIVE/INACTIVE/SUSPENDED/DELETED)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "회원 상태 변경 완료",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 유저",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "존재하지 않는 이메일입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> updateUserStatus(
            @PathVariable("userId") Long userId,
            @RequestBody AdminUserDto.RequestUpdateStatus request
    );
}