package kr.co.mapspring.user.oauth.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.oauth.dto.OauthLoginDto;

public interface OauthControllerDocs {

    @Operation(
            summary = "OAuth 로그인 토큰 교환",
            description = """
                    Google OAuth 로그인 성공 후 발급된 1회용 code를 Access Token으로 교환합니다.
                    Refresh Token은 응답 body에 포함하지 않고 HttpOnly Cookie로 설정합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OAuth 토큰 교환 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "OAuth 토큰 교환 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "OAuth 토큰 교환 성공",
                                              "data": {
                                                "accessToken": "<ACCESS_TOKEN>",
                                                "profileRequired": true
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않거나 만료된 OAuth 로그인 code",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "유효하지 않은 code",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 400,
                                              "message": "유효하지 않거나 만료된 OAuth 로그인 코드입니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ApiResponseDTO<OauthLoginDto.ResponseToken> exchangeToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "OAuth 토큰 교환 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OauthLoginDto.RequestToken.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": "3f1b8f1e-4f4a-4c25-9c91-123456789abc"
                                            }
                                            """
                            )
                    )
            )
            OauthLoginDto.RequestToken request,

            @Parameter(hidden = true)
            HttpServletResponse httpServletResponse
    );
}