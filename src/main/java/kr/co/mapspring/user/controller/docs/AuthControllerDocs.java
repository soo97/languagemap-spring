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
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.dto.TokenDto;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 입력받아 로그인 처리하고 사용자 정보 및 토큰 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그인 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "로그인 성공",
                                              "data": {
                                                "userId": 1,
                                                "email": "test@naver.com",
                                                "name": "홍길동",
                                                "role": "USER",
                                                "accessToken": "<ACCESS_TOKEN>",
                                                "refreshToken": "<REFRESH_TOKEN>"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 이메일",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "존재하지 않는 이메일",
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
                                    name = "비밀번호 불일치",
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
                                    name = "비활성 사용자",
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

    @Operation(
            summary = "회원가입",
            description = "이름, 생년월일, 주소, 전화번호, 이메일, 비밀번호 및 약관 동의 여부를 입력받아 회원가입을 처리합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원가입 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "회원가입 성공",
                                              "data": {
                                                "userId": 1,
                                                "email": "test@naver.com",
                                                "name": "홍길동"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원가입 요청값 오류",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "비밀번호 확인 불일치",
                                            summary = "password와 passwordConfirm이 일치하지 않는 경우",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "message": "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
                                                      "data": null
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "입력값 검증 실패",
                                            summary = "생년월일 형식 오류 등 요청값 검증에 실패한 경우",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "message": "입력값 검증 실패",
                                                      "data": null
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "서비스 이용약관 미동의",
                                            summary = "serviceAgree가 false인 경우",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "message": "서비스 이용약관 동의는 필수입니다.",
                                                      "data": null
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "개인정보 수집 및 이용 미동의",
                                            summary = "privacyAgree가 false인 경우",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 400,
                                                      "message": "개인정보 수집 및 이용 동의는 필수입니다.",
                                                      "data": null
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활성 약관 정보 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "활성 약관 정보 없음",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 404,
                                              "message": "활성 약관 정보를 찾을 수 없습니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "회원가입 중복 리소스",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "이미 존재하는 이메일",
                                            summary = "email이 이미 존재하는 경우",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 409,
                                                      "message": "이미 존재하는 이메일입니다.",
                                                      "data": null
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "이미 존재하는 전화번호",
                                            summary = "phoneNumber가 이미 존재하는 경우",
                                            value = """
                                                    {
                                                      "success": false,
                                                      "status": 409,
                                                      "message": "이미 존재하는 전화번호입니다.",
                                                      "data": null
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    ApiResponseDTO<SignUpDto.ResponseSignUp> signUp(
            @RequestBody(
                    description = "회원가입 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpDto.RequestSignUp.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "name": "홍길동",
                                              "birthDate": "2000-01-01",
                                              "address": "서울시 강남구",
                                              "phoneNumber": "010-0000-0001",
                                              "email": "test@test1.com",
                                              "password": "1234",
                                              "passwordConfirm": "1234",
                                              "serviceAgree": true,
                                              "privacyAgree": true,
                                              "marketingAgree": false
                                            }
                                            """
                            )
                    )
            )
            SignUpDto.RequestSignUp request
    );

    @Operation(
            summary = "Access Token 및 Refresh Token 재발급",
            description = "Refresh Token을 검증한 뒤 새로운 Access Token과 Refresh Token을 발급합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "토큰 재발급 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "토큰 재발급 성공",
                                              "data": {
                                                "accessToken": "<NEW_ACCESS_TOKEN>",
                                                "refreshToken": "<NEW_REFRESH_TOKEN>"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 Refresh Token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "유효하지 않은 Refresh Token",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 401,
                                              "message": "Refresh Token이 유효하지 않습니다.",
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
                                    name = "비활성 사용자",
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
    ApiResponseDTO<TokenDto.ResponseReissue> reissueToken(
            @RequestBody(
                    description = "토큰 재발급 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenDto.RequestReissue.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "refreshToken": "<REFRESH_TOKEN>"
                                            }
                                            """
                            )
                    )
            )
            TokenDto.RequestReissue request
    );

    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 검증한 뒤 Redis에 저장된 Refresh Token을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "로그아웃 성공",
                                    value = """
                                            {
                                              "success": true,
                                              "status": 200,
                                              "message": "로그아웃 성공",
                                              "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 Refresh Token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "유효하지 않은 Refresh Token",
                                    value = """
                                            {
                                              "success": false,
                                              "status": 401,
                                              "message": "Refresh Token이 유효하지 않습니다.",
                                              "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ApiResponseDTO<Void> logout(
            @RequestBody(
                    description = "로그아웃 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenDto.RequestLogout.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "refreshToken": "<REFRESH_TOKEN>"
                                            }
                                            """
                            )
                    )
            )
            TokenDto.RequestLogout request
    );
}