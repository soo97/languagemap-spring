package kr.co.mapspring.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.co.mapspring.global.jwt.JwtAuthenticationFilter;
import kr.co.mapspring.user.oauth.handler.OauthLoginFailureHandler;
import kr.co.mapspring.user.oauth.handler.OauthLoginSuccessHandler;
import kr.co.mapspring.user.oauth.service.OauthUserService;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OauthUserService oauthUserService;
    private final OauthLoginSuccessHandler oauthLoginSuccessHandler;
    private final OauthLoginFailureHandler oauthLoginFailureHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OauthUserService oauthUserService,
            OauthLoginSuccessHandler oauthLoginSuccessHandler,
            OauthLoginFailureHandler oauthLoginFailureHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oauthUserService = oauthUserService;
        this.oauthLoginSuccessHandler = oauthLoginSuccessHandler;
        this.oauthLoginFailureHandler = oauthLoginFailureHandler;
    }

    private static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/tokens",
            "/api/auth/logout",
            "/api/auth/oauth/tokens",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/ws/**",
            "/oauth2/**",
            "/login/oauth2/**"
    };

    private static final String[] USER_URLS = {
            // ex "/api/users/me"
    };

    private static final String[] ADMIN_URLS = {
            // ex "/api/admin/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth
                        // CORS preflight 요청 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 비로그인 접근 허용 API
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // 관리자 전용 API
                        .requestMatchers(ADMIN_URLS).hasRole("ADMIN")

                        // 일반 사용자 또는 관리자 접근 API
                        .requestMatchers(USER_URLS).hasAnyRole("USER", "ADMIN")

                        // 팀 개발 중 임시 허용
                        .anyRequest().permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauthUserService)
                        )
                        .successHandler(oauthLoginSuccessHandler)
                        .failureHandler(oauthLoginFailureHandler)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "https://d2tm0yjtieb89r.cloudfront.net",
                "http://localhost:5173",
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "http://localhost:80",
                "http://localhost"
        ));

        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}