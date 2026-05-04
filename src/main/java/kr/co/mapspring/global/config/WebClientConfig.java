package kr.co.mapspring.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {

	@Bean
    public WebClient fastApiWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }
}
