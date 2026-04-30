package kr.co.mapspring.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync // @Async 활성화
public class AsyncConfig {

    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
 
        // 기본 스레드 수 - 평상시 유지할 스레드 수
        executor.setCorePoolSize(5);
 
        // 최대 스레드 수 - 동시 요청이 몰릴 때 최대로 늘어날 스레드 수
        executor.setMaxPoolSize(20);
 
        // 큐 용량 - 스레드가 꽉 찼을 때 대기할 수 있는 작업 수
        executor.setQueueCapacity(100);
 
        // 스레드 이름 prefix - 로그에서 식별하기 쉽게
        executor.setThreadNamePrefix("email-task-");
 
        // 앱 종료 시 진행 중인 이메일 발송이 완료될 때까지 대기
        executor.setWaitForTasksToCompleteOnShutdown(true);
 
        // 종료 대기 시간 (초)
        executor.setAwaitTerminationSeconds(30);
 
        executor.initialize();
        return executor;
    }
}
