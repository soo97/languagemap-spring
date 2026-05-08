package kr.co.mapspring.place.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import kr.co.mapspring.place.dto.fastapi.FastApiChatDto;
import kr.co.mapspring.place.dto.fastapi.FastApiEvaluationDto;
import kr.co.mapspring.place.dto.fastapi.FastApiMissionStartDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FastApiClient {
	
	private final WebClient webClient;
	
	public FastApiMissionStartDto.ResponseMissionStart missionStart(FastApiMissionStartDto.RequestMissionStart request) {
	    return webClient.post()
	            .uri("/gpt/mission/start")
	            .bodyValue(request)
	            .retrieve()
	            .bodyToMono(FastApiMissionStartDto.ResponseMissionStart.class)
	            .block();

	}
	
	public FastApiChatDto.ResponseChat chat(FastApiChatDto.RequestChat request) {
	    return webClient.post()
	            .uri("/gpt/chat")
	            .bodyValue(request)
	            .retrieve()
	            .bodyToMono(FastApiChatDto.ResponseChat.class)
	            .block();
	}
	
	public FastApiEvaluationDto.ResponseEvaluation evaluate(FastApiEvaluationDto.RequestEvaluation request) {
	    return webClient.post()
	            .uri("/gpt/evaluate")
	            .bodyValue(request)
	            .retrieve()
	            .bodyToMono(FastApiEvaluationDto.ResponseEvaluation.class)
	            .block();
	}
}
