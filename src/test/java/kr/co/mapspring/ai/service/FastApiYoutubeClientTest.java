package kr.co.mapspring.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import kr.co.mapspring.ai.client.FastApiYoutubeClient;
import kr.co.mapspring.ai.dto.FastApiYoutubeDto;
import kr.co.mapspring.global.exception.ai.FastApiAiClientException;

@ExtendWith(MockitoExtension.class)
class FastApiYoutubeClientTest {

    @InjectMocks
    private FastApiYoutubeClient fastApiYoutubeClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws Exception {
        setField(fastApiYoutubeClient, "fastApiBaseUrl", "http://localhost:8000");
    }

    @Test
    @DisplayName("유튜브 검색 요청 성공")
    void 유튜브_검색_요청_성공() {
        // given
        FastApiYoutubeDto.RequestYoutubeSearch request =
                FastApiYoutubeDto.RequestYoutubeSearch.builder()
                        .keyword("English cafe conversation")
                        .maxResults(3)
                        .build();

        FastApiYoutubeDto.ResponseYoutubeSearch expectedResponse =
                new FastApiYoutubeDto.ResponseYoutubeSearch();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/youtube-search"),
                eq(request),
                eq(FastApiYoutubeDto.ResponseYoutubeSearch.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiYoutubeDto.ResponseYoutubeSearch response =
                fastApiYoutubeClient.searchYoutube(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("영상 요약 요청 성공")
    void 영상_요약_요청_성공() {
        // given
        FastApiYoutubeDto.RequestVideoSummary request =
                FastApiYoutubeDto.RequestVideoSummary.builder()
                        .title("Cafe English Conversation")
                        .channelTitle("English Channel")
                        .description("Practice useful cafe expressions.")
                        .build();

        FastApiYoutubeDto.ResponseVideoSummary expectedResponse =
                new FastApiYoutubeDto.ResponseVideoSummary();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/video-summary"),
                eq(request),
                eq(FastApiYoutubeDto.ResponseVideoSummary.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiYoutubeDto.ResponseVideoSummary response =
                fastApiYoutubeClient.createVideoSummary(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("FastAPI 응답이 null이면 예외가 발생한다")
    void FastAPI_응답이_null이면_예외_발생() {
        // given
        FastApiYoutubeDto.RequestYoutubeSearch request =
                FastApiYoutubeDto.RequestYoutubeSearch.builder()
                        .keyword("test")
                        .maxResults(1)
                        .build();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/youtube-search"),
                eq(request),
                eq(FastApiYoutubeDto.ResponseYoutubeSearch.class)
        )).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> fastApiYoutubeClient.searchYoutube(request))
                .isInstanceOf(FastApiAiClientException.class);
    }

    @Test
    @DisplayName("FastAPI 호출 중 RestClientException 발생 시 FastApiAiClientException으로 변환한다")
    void FastAPI_호출_실패시_FastApiAiClientException_발생() {
        // given
        FastApiYoutubeDto.RequestVideoSummary request =
                FastApiYoutubeDto.RequestVideoSummary.builder()
                        .title("title")
                        .channelTitle("channel")
                        .description("description")
                        .build();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/video-summary"),
                eq(request),
                eq(FastApiYoutubeDto.ResponseVideoSummary.class)
        )).thenThrow(new RestClientException("FastAPI connection failed"));

        // when & then
        assertThatThrownBy(() -> fastApiYoutubeClient.createVideoSummary(request))
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