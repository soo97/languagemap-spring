package kr.co.mapspring.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.dto.UserDto;

@Tag(name = "User", description = "유저 관련 API")
public interface UserControllerDocs {

    @Operation(
            summary = "내 정보 조회",
            description = """
                    현재 로그인한 유저의 정보를 조회합니다.
                    Authorization 헤더에 Bearer Access Token이 필요합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "내 정보 조회 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "내 정보 조회 성공",
                                              "data": {
                                                "userId": 1,
                                                "email": "test@naver.com",
                                                "name": "홍길동",
                                                "role": "USER",
                                                "status": "ACTIVE"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰 없음 또는 유효하지 않은 토큰",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 401,
                                              "message": "인증이 필요합니다.",
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
                                    name = "유저 없음",
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
    ApiResponseDTO<UserDto.ResponseMe> getMe(
            @Parameter(hidden = true)
            HttpServletRequest request
    );
    
    
    @Operation(
            summary = "소셜 유저 프로필 입력",
            description = """
                    소셜 로그인으로 가입한 유저가 최초 1회 프로필을 입력합니다.
                    생년월일, 주소, 전화번호를 입력받아 저장합니다.
                    Authorization 헤더에 Bearer Access Token이 필요합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 입력 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "프로필 입력 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "프로필 입력 성공",
                                              "data": {
                                                "userId": 1,
                                                "email": "test@gmail.com",
                                                "name": "홍길동"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력값 검증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "입력값 검증 실패",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 400,
                                              "message": "입력값 검증 실패",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 401,
                                              "message": "인증이 필요합니다.",
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
                                    name = "유저 없음",
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
    ApiResponseDTO<UserDto.ResponseProfileSetup> setupProfile(
            @Parameter(hidden = true)
            HttpServletRequest request,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "프로필 입력 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.RequestProfileSetup.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "birthDate": "2000-01-01",
                                              "address": "서울시 강남구",
                                              "phoneNumber": "010-1234-5678"
                                            }
                                            """
                            )
                    )
            )
            UserDto.RequestProfileSetup profileRequest
    );
    
    
    
    
    
    
    
    
}