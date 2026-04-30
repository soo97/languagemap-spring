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

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /*
     * 인증 없이 접근 가능한 API
     * 예: 로그인, 회원가입, 토큰 재발급, 로그아웃, Swagger
     */
    private static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/tokens",
            "/api/auth/logout",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/ws/**"
    };
    
    

    /*
     * 로그인한 일반 사용자 또는 관리자만 접근 가능한 API
     * 팀원들이 로그인 사용자 전용 API를 만들면 여기에 추가한다.
     *
     * 예:
     * "/api/users/me",
     * "/api/favorite-places/**",
     * "/api/favorite-scenarios/**"
     */
    private static final String[] USER_URLS = {
//        ex    "/api/users/me"
    };
    
    

    /*
     * 관리자만 접근 가능한 API
     * 추후 관리자 페이지/API를 만들면 /api/admin/** 하위로 두는 것을 권장한다.
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

                // JWT 기반 인증은 서버 세션을 사용하지 않음
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
                         *
                         * 추후 전체 API 경로 정리가 끝나면 아래를 authenticated()로 변경한다.
                         *
                         * 현재:
                         * .anyRequest().permitAll()
                         *
                         * 최종:
                         * .anyRequest().authenticated()
                         */
                        .anyRequest().permitAll()
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
                "http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}