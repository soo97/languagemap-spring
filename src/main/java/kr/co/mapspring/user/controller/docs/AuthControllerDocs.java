package kr.co.mapspring.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.dto.LoginDto;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 입력받아 로그인 처리하고 사용자 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 이메일",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "USER_NOT_FOUND",
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
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "비밀번호 불일치",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "INVALID_PASSWORD",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 401,
                                              "message": "비밀번호가 일치하지 않습니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "비활성 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "INACTIVE_USER",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 403,
                                              "message": "비활성 사용자입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ApiResponseDTO<LoginDto.ResponseLogin> login(
            @RequestBody(
                    description = "로그인 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDto.RequestLogin.class),
                            examples = @ExampleObject(
                                    name = "로그인 요청 예시",
                                    value = """
                                            {
                                              "email": "test@naver.com",
                                              "password": "1234"
                                            }
                                            """
                            )
                    )
            )
            LoginDto.RequestLogin request
    );
}