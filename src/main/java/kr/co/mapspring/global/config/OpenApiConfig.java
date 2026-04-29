package kr.co.mapspring.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    // Swagger UI에서 사용할 JWT 인증 스키마 이름
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // Swagger 전체 API에 bearerAuth 보안 스키마를 적용한다.
                // 현재 SecurityConfig는 B안(permitAll)이므로 실제 API 접근은 막지 않는다.
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // Swagger UI에 Authorize 버튼을 만들기 위한 보안 스키마 설정
                .components(new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        // HTTP 인증 방식 사용
                                        .type(SecurityScheme.Type.HTTP)

                                        // Bearer Token 방식 사용
                                        .scheme("bearer")

                                        // JWT 형식의 Bearer Token임을 표시
                                        .bearerFormat("JWT")
                        )
                );
    }
}