package kr.co.mapspring.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import kr.co.mapspring.ai.client.FastApiSpeechClient;
import kr.co.mapspring.ai.dto.FastApiSpeechDto;
import kr.co.mapspring.global.exception.ai.FastApiAiClientException;

@ExtendWith(MockitoExtension.class)
class FastApiSpeechClientTest {

    @InjectMocks
    private FastApiSpeechClient fastApiSpeechClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MultipartFile audioFile;

    @BeforeEach
    void setUp() throws Exception {
        setField(fastApiSpeechClient, "fastApiBaseUrl", "http://localhost:8000");
    }

    @Test
    @DisplayName("TTS 생성 요청 성공")
    void TTS_생성_요청_성공() {
        // given
        FastApiSpeechDto.RequestTts request =
                FastApiSpeechDto.RequestTts.builder()
                        .text("Hello")
                        .build();

        FastApiSpeechDto.ResponseTts expectedResponse =
                new FastApiSpeechDto.ResponseTts();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/tts"),
                eq(request),
                eq(FastApiSpeechDto.ResponseTts.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiSpeechDto.ResponseTts response =
                fastApiSpeechClient.createTts(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("문제 단어 음성 생성 요청 성공")
    void 문제_단어_음성_생성_요청_성공() {
        // given
        FastApiSpeechDto.RequestProblemWordAudio request =
                FastApiSpeechDto.RequestProblemWordAudio.builder()
                        .word("latte")
                        .build();

        FastApiSpeechDto.ResponseProblemWordAudio expectedResponse =
                new FastApiSpeechDto.ResponseProblemWordAudio();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/problem-word-audio"),
                eq(request),
                eq(FastApiSpeechDto.ResponseProblemWordAudio.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiSpeechDto.ResponseProblemWordAudio response =
                fastApiSpeechClient.createProblemWordAudio(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("STT 요청 성공")
    void STT_요청_성공() throws Exception {
        // given
        when(audioFile.getBytes()).thenReturn("audio".getBytes());
        when(audioFile.getOriginalFilename()).thenReturn("test.wav");

        FastApiSpeechDto.ResponseStt expectedResponse =
                new FastApiSpeechDto.ResponseStt();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/stt"),
                any(HttpEntity.class),
                eq(FastApiSpeechDto.ResponseStt.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiSpeechDto.ResponseStt response =
                fastApiSpeechClient.createStt(audioFile);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("발음 평가 요청 성공")
    void 발음_평가_요청_성공() throws Exception {
        // given
        when(audioFile.getBytes()).thenReturn("audio".getBytes());
        when(audioFile.getOriginalFilename()).thenReturn("test.wav");

        FastApiSpeechDto.ResponsePronunciationAssessment expectedResponse =
                new FastApiSpeechDto.ResponsePronunciationAssessment();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/pronunciation-assessment"),
                any(HttpEntity.class),
                eq(FastApiSpeechDto.ResponsePronunciationAssessment.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiSpeechDto.ResponsePronunciationAssessment response =
                fastApiSpeechClient.assessPronunciation("I would like a latte.", audioFile);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("FastAPI 응답이 null이면 예외가 발생한다")
    void FastAPI_응답이_null이면_예외_발생() {
        // given
        FastApiSpeechDto.RequestTts request =
                FastApiSpeechDto.RequestTts.builder()
                        .text("Hello")
                        .build();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/tts"),
                eq(request),
                eq(FastApiSpeechDto.ResponseTts.class)
        )).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> fastApiSpeechClient.createTts(request))
                .isInstanceOf(FastApiAiClientException.class);
    }

    @Test
    @DisplayName("FastAPI 호출 중 RestClientException 발생 시 FastApiAiClientException으로 변환한다")
    void FastAPI_호출_실패시_FastApiAiClientException_발생() {
        // given
        FastApiSpeechDto.RequestTts request =
                FastApiSpeechDto.RequestTts.builder()
                        .text("Hello")
                        .build();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/tts"),
                eq(request),
                eq(FastApiSpeechDto.ResponseTts.class)
        )).thenThrow(new RestClientException("FastAPI connection failed"));

        // when & then
        assertThatThrownBy(() -> fastApiSpeechClient.createTts(request))
                .isInstanceOf(FastApiAiClientException.class)
                .hasCauseInstanceOf(RestClientException.class);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;

        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }

        throw new NoSuchFieldException(fieldName);
    }
}