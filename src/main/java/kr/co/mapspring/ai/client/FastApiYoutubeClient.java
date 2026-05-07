package kr.co.mapspring.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import kr.co.mapspring.ai.dto.FastApiYoutubeDto;
import kr.co.mapspring.global.exception.ai.FastApiAiClientException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FastApiYoutubeClient {

    private final RestTemplate restTemplate;

    @Value("${fastapi.base-url}")
    private String fastApiBaseUrl;

    public FastApiYoutubeDto.ResponseYoutubeSearch searchYoutube(
            FastApiYoutubeDto.RequestYoutubeSearch request
    ) {
        return postJson(
                "/api/ai-coaching/youtube-search",
                request,
                FastApiYoutubeDto.ResponseYoutubeSearch.class
        );
    }

    public FastApiYoutubeDto.ResponseVideoSummary createVideoSummary(
            FastApiYoutubeDto.RequestVideoSummary request
    ) {
        return postJson(
                "/api/ai-coaching/video-summary",
                request,
                FastApiYoutubeDto.ResponseVideoSummary.class
        );
    }

    private <T> T postJson(String path, Object request, Class<T> responseType) {
        try {
            T response = restTemplate.postForObject(
                    createUrl(path),
                    request,
                    responseType
            );

            if (response == null) {
                throw new FastApiAiClientException();
            }

            return response;
        } catch (RestClientException e) {
            throw new FastApiAiClientException(e);
        }
    }

    private String createUrl(String path) {
        if (fastApiBaseUrl.endsWith("/")) {
            return fastApiBaseUrl.substring(0, fastApiBaseUrl.length() - 1) + path;
        }

        return fastApiBaseUrl + path;
    }
}