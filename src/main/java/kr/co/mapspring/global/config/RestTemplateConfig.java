package kr.co.mapspring.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // FastAPI 등 외부 API 호출에 사용할 RestTemplate Bean 등록
    // Client 클래스에서 직접 new RestTemplate()으로 생성하지 않음
    // Spring Bean으로 주입받아 사용하기 위한 설정
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}