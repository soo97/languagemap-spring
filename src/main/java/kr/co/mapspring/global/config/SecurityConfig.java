package kr.co.mapspring.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    /*
     * 인증 없이 접근 가능한 API
     * 예: 로그인, 회원가입, 토큰 재발급, 로그아웃, Swagger, OAuth 시작/콜백
     */
    private static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/tokens",
            "/api/auth/logout",
            "/api/auth/oauth/tokens",
            "/swagger-ui/**",
            "/v3/api-docs/**",

            // WebSocket 허용
            "/ws/**",
            

            // Google OAuth 시작 경로: /oauth2/authorization/google
            "/oauth2/**",

            // Google OAuth callback 경로: /login/oauth2/code/google
            "/login/oauth2/**"
    };

    /*
     * 로그인한 일반 사용자 또는 관리자만 접근 가능한 API
     */
    private static final String[] USER_URLS = {
//        ex    "/api/users/me"
    };

    /*
     * 관리자만 접근 가능한 API
     */
    private static final String[] ADMIN_URLS = {
//        ex    "/api/admin/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API 방식이므로 CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // CORS 설정 연결
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                /*
                 * JWT 기반 API는 세션을 쓰지 않지만,
                 * OAuth2 Login은 Google 인증 왕복 과정에서 세션이 필요할 수 있습니다.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth
                        // 1. 비로그인 사용자도 접근 가능한 API
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // 2. 관리자 전용 API
                        .requestMatchers(ADMIN_URLS).hasRole("ADMIN")

                        // 3. 로그인 사용자 또는 관리자 접근 가능 API
                        .requestMatchers(USER_URLS).hasAnyRole("USER", "ADMIN")

                        /*
                         * 4. 아직 팀원들이 API 개발 중이므로 나머지는 임시 허용
                         * 추후 전체 API 경로 정리 후 authenticated()로 전환합니다.
                         */
                        .anyRequest().permitAll()
                )

                /*
                 * Google OAuth2 Login 설정입니다.
                 * Google 사용자 정보 조회 후 OauthUserService에서
                 * User 생성/조회 및 oauth_account 연결을 처리합니다.
                 */
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauthUserService)
                        )
                        .successHandler(oauthLoginSuccessHandler)
                        .failureHandler(oauthLoginFailureHandler)
                )

                // JWT 인증 필터 등록
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
                "http://localhost:5173",
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "http://localhost:80",
                "http://localhost"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}