package kr.co.mapspring.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * OAuth 로그인 코드 저장/조회 과정에서 JSON 직렬화·역직렬화에 사용할 ObjectMapper Bean입니다.
     * 
     * OauthLoginCodeService에서 ObjectMapper를 생성자 주입받고 있으므로,
     * Spring Container에 ObjectMapper Bean이 반드시 등록되어 있어야 합니다.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // LocalDateTime, LocalDate 등 Java 8 time 타입 직렬화 지원
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}