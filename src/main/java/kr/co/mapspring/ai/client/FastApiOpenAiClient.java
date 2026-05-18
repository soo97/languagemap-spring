package kr.co.mapspring.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import kr.co.mapspring.ai.dto.FastApiOpenAiDto;
import kr.co.mapspring.global.exception.ai.FastApiAiClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class FastApiOpenAiClient {

    private final RestTemplate restTemplate;

    @Value("${fastapi.base-url}")
    private String fastApiBaseUrl;

    public FastApiOpenAiDto.ResponseCoachingScript createCoachingScript(
            FastApiOpenAiDto.RequestCoachingScript request
    ) {
        return postJson(
                "/api/ai-coaching/coaching-script",
                request,
                FastApiOpenAiDto.ResponseCoachingScript.class
        );
    }

    public FastApiOpenAiDto.ResponseFinalFeedback createFinalFeedback(
            FastApiOpenAiDto.RequestFinalFeedback request
    ) {
        return postJson(
                "/api/ai-coaching/final-feedback",
                request,
                FastApiOpenAiDto.ResponseFinalFeedback.class
        );
    }

    public FastApiOpenAiDto.ResponseYoutubeKeywords createYoutubeKeywords(
            FastApiOpenAiDto.RequestYoutubeKeywords request
    ) {
        return postJson(
                "/api/ai-coaching/youtube-keywords",
                request,
                FastApiOpenAiDto.ResponseYoutubeKeywords.class
        );
    }

    private <T> T postJson(String path, Object request, Class<T> responseType) {
        String url = createUrl(path);

        try {
            log.info("Calling FastAPI OpenAI endpoint. path={}, url={}", path, url);

            T response = restTemplate.postForObject(
                    url,
                    request,
                    responseType
            );

            if (response == null) {
                throw new FastApiAiClientException();
            }

            return response;
        } catch (RestClientException e) {
            Throwable rootCause = getRootCause(e);
            log.error(
                    "FastAPI OpenAI request failed. path={}, url={}, causeClass={}, causeMessage={}",
                    path,
                    url,
                    rootCause.getClass().getName(),
                    rootCause.getMessage()
            );
            throw new FastApiAiClientException(e);
        }
    }

    private String createUrl(String path) {
        if (fastApiBaseUrl.endsWith("/")) {
            return fastApiBaseUrl.substring(0, fastApiBaseUrl.length() - 1) + path;
        }
        return fastApiBaseUrl + path;
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;

        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        return rootCause;
    }
}
