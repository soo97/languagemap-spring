package kr.co.mapspring.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mapspring.ai.dto.FastApiSpeechDto;
import kr.co.mapspring.global.exception.ai.FastApiAiClientException;
import lombok.RequiredArgsConstructor;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class FastApiSpeechClient {

    private final RestTemplate restTemplate;

    @Value("${fastapi.base-url}")
    private String fastApiBaseUrl;

    public FastApiSpeechDto.ResponseTts createTts(
            FastApiSpeechDto.RequestTts request
    ) {
        return postJson(
                "/api/ai-coaching/tts",
                request,
                FastApiSpeechDto.ResponseTts.class
        );
    }

    public FastApiSpeechDto.ResponseProblemWordAudio createProblemWordAudio(
            FastApiSpeechDto.RequestProblemWordAudio request
    ) {
        return postJson(
                "/api/ai-coaching/problem-word-audio",
                request,
                FastApiSpeechDto.ResponseProblemWordAudio.class
        );
    }

    public FastApiSpeechDto.ResponseStt createStt(MultipartFile audioFile) {
        return postMultipart(
                "/api/ai-coaching/stt",
                createAudioOnlyMultipart(audioFile),
                FastApiSpeechDto.ResponseStt.class
        );
    }

    public FastApiSpeechDto.ResponsePronunciationAssessment assessPronunciation(
            String referenceText,
            MultipartFile audioFile
    ) {
        return postMultipart(
                "/api/ai-coaching/pronunciation-assessment",
                createPronunciationMultipart(referenceText, audioFile),
                FastApiSpeechDto.ResponsePronunciationAssessment.class
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

    private <T> T postMultipart(
            String path,
            MultiValueMap<String, HttpEntity<?>> multipartBody,
            Class<T> responseType
    ) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity =
                    new HttpEntity<>(multipartBody, headers);

            T response = restTemplate.postForObject(
                    createUrl(path),
                    requestEntity,
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

    private MultiValueMap<String, HttpEntity<?>> createAudioOnlyMultipart(
            MultipartFile audioFile
    ) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("audio_file", toByteArrayResource(audioFile))
                .filename(audioFile.getOriginalFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        return builder.build();
    }

    private MultiValueMap<String, HttpEntity<?>> createPronunciationMultipart(
            String referenceText,
            MultipartFile audioFile
    ) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("reference_text", referenceText);
        builder.part("audio_file", toByteArrayResource(audioFile))
                .filename(audioFile.getOriginalFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        return builder.build();
    }

    private ByteArrayResource toByteArrayResource(MultipartFile file) {
        try {
            return new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
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