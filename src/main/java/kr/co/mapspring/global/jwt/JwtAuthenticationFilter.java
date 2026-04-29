package kr.co.mapspring.global.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Authorization 헤더에서 Bearer 토큰을 꺼낸다.
        String token = resolveToken(request);

        // 일단은 다른 기능들 테스트를위해 토큰이 없어도 요청을 막지 않고 그대로 통과
        // 추후 기능들이 얼추 마무리 될 시 필터 걸 예정
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Access Token이 정상일 때만 인증 정보를 세팅한다.
            if (jwtTokenProvider.validateAccessToken(token)) {
                Long userId = jwtTokenProvider.getUserId(token);

                userRepository.findById(userId)
                        .filter(User::isActive)
                        .ifPresent(user -> {
                            // Spring Security 권한 형식에 맞춰 ROLE_ prefix를 붙인다.
                            List<SimpleGrantedAuthority> authorities = List.of(
                                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                            );

                            // 인증 객체 생성
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(user, null, authorities);

                            // 요청 정보 세팅
                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            // SecurityContext에 인증 정보 저장
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        });
            }
        } catch (Exception e) {
            // B안에서는 잘못된 토큰이 들어와도 전체 요청을 막지 않는다.
            // 대신 인증 정보만 제거하고 다음 필터로 넘긴다.
            SecurityContextHolder.clearContext();
        }

        // 다음 필터 또는 Controller로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Authorization: Bearer {token} 형식에서 token 부분만 꺼낸다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}