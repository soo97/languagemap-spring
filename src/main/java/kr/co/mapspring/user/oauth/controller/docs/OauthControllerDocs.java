package kr.co.mapspring.user.oauth.controller.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.oauth.dto.OauthLoginDto;

public interface OauthControllerDocs {

    @Operation(
            summary = "OAuth 로그인 토큰 교환",
            description = "Google OAuth 로그인 성공 후 발급된 1회용 code를 accessToken/refreshToken으로 교환합니다."
    )
    ApiResponseDTO<OauthLoginDto.ResponseToken> exchangeToken(
            @Valid @RequestBody OauthLoginDto.RequestToken request
    );
}