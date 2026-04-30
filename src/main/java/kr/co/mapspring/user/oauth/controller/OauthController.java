package kr.co.mapspring.user.oauth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.oauth.controller.docs.OauthControllerDocs;
import kr.co.mapspring.user.oauth.dto.OauthLoginDto;
import kr.co.mapspring.user.oauth.service.OauthLoginCodeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/oauth")
@RequiredArgsConstructor
public class OauthController implements OauthControllerDocs {

    private final OauthLoginCodeService oauthLoginCodeService;

    @Override
    @PostMapping("/tokens")
    public ApiResponseDTO<OauthLoginDto.ResponseToken> exchangeToken(
            @Valid @RequestBody OauthLoginDto.RequestToken request
    ) {
        OauthLoginDto.ResponseToken response =
                oauthLoginCodeService.consumeToken(request.getCode());

        return ApiResponseDTO.success("OAuth 토큰 교환 성공", response);
    }
}