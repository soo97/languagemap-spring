package kr.co.mapspring.place.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import kr.co.mapspring.place.dto.fastapi.FastApiMissionStartDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FastApiClient {
	
	private final WebClient webClient;
	
	public FastApiMissionStartDto.ResponseMissionStart missionStart(FastApiMissionStartDto.RequestMissionStart request) {
	    return webClient.post()
	            .uri("gpt/mission/start")
	            .bodyValue(request)
	            .retrieve()
	            .bodyToMono(FastApiMissionStartDto.ResponseMissionStart.class)
	            .block();

	}
}
