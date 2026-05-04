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

import kr.co.mapspring.ai.client.FastApiOpenAiClient;
import kr.co.mapspring.ai.dto.FastApiOpenAiDto;
import kr.co.mapspring.global.exception.ai.FastApiAiClientException;

@ExtendWith(MockitoExtension.class)
class FastApiOpenAiClientTest {

    @InjectMocks
    private FastApiOpenAiClient fastApiOpenAiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws Exception {
        setField(fastApiOpenAiClient, "fastApiBaseUrl", "http://localhost:8000");
    }

    @Test
    @DisplayName("코칭 스크립트 생성 요청 성공")
    void 코칭_스크립트_생성_요청_성공() {
        // given
        FastApiOpenAiDto.RequestCoachingScript request =
                FastApiOpenAiDto.RequestCoachingScript.builder()
                        .optionType("DIALOGUE")
                        .placeName("Cafe Stage 888")
                        .country("Australia")
                        .city("Sydney")
                        .placeAddress("Sydney CBD")
                        .evaluation("표현 좋음, 속도 개선 필요")
                        .previousMessages(null)
                        .build();

        FastApiOpenAiDto.ResponseCoachingScript expectedResponse =
                new FastApiOpenAiDto.ResponseCoachingScript();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/coaching-script"),
                eq(request),
                eq(FastApiOpenAiDto.ResponseCoachingScript.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiOpenAiDto.ResponseCoachingScript response =
                fastApiOpenAiClient.createCoachingScript(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("최종 피드백 생성 요청 성공")
    void 최종_피드백_생성_요청_성공() {
        // given
        FastApiOpenAiDto.RequestFinalFeedback request =
                FastApiOpenAiDto.RequestFinalFeedback.builder()
                        .messages(null)
                        .pronunciationResults(null)
                        .build();

        FastApiOpenAiDto.ResponseFinalFeedback expectedResponse =
                new FastApiOpenAiDto.ResponseFinalFeedback();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/final-feedback"),
                eq(request),
                eq(FastApiOpenAiDto.ResponseFinalFeedback.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiOpenAiDto.ResponseFinalFeedback response =
                fastApiOpenAiClient.createFinalFeedback(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("추천 문장 생성 요청 성공")
    void 추천_문장_생성_요청_성공() {
        // given
        FastApiOpenAiDto.RequestRecommendSentences request =
                FastApiOpenAiDto.RequestRecommendSentences.builder()
                        .finalFeedback("표현은 자연스럽지만 더 다양한 문장을 연습하면 좋습니다.")
                        .build();

        FastApiOpenAiDto.ResponseRecommendSentences expectedResponse =
                new FastApiOpenAiDto.ResponseRecommendSentences();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/recommend-sentences"),
                eq(request),
                eq(FastApiOpenAiDto.ResponseRecommendSentences.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiOpenAiDto.ResponseRecommendSentences response =
                fastApiOpenAiClient.recommendSentences(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("유튜브 키워드 생성 요청 성공")
    void 유튜브_키워드_생성_요청_성공() {
        // given
        FastApiOpenAiDto.RequestYoutubeKeywords request =
                FastApiOpenAiDto.RequestYoutubeKeywords.builder()
                        .finalFeedback("카페 주문 상황에서 사용할 수 있는 표현을 더 연습하면 좋습니다.")
                        .build();

        FastApiOpenAiDto.ResponseYoutubeKeywords expectedResponse =
                new FastApiOpenAiDto.ResponseYoutubeKeywords();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/youtube-keywords"),
                eq(request),
                eq(FastApiOpenAiDto.ResponseYoutubeKeywords.class)
        )).thenReturn(expectedResponse);

        // when
        FastApiOpenAiDto.ResponseYoutubeKeywords response =
                fastApiOpenAiClient.createYoutubeKeywords(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("FastAPI 응답이 null이면 예외가 발생한다")
    void FastAPI_응답이_null이면_예외_발생() {
        // given
        FastApiOpenAiDto.RequestRecommendSentences request =
                FastApiOpenAiDto.RequestRecommendSentences.builder()
                        .finalFeedback("feedback")
                        .build();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/recommend-sentences"),
                eq(request),
                eq(FastApiOpenAiDto.ResponseRecommendSentences.class)
        )).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> fastApiOpenAiClient.recommendSentences(request))
                .isInstanceOf(FastApiAiClientException.class);
    }

    @Test
    @DisplayName("FastAPI 호출 중 RestClientException 발생 시 FastApiAiClientException으로 변환한다")
    void FastAPI_호출_실패시_FastApiAiClientException_발생() {
        // given
        FastApiOpenAiDto.RequestYoutubeKeywords request =
                FastApiOpenAiDto.RequestYoutubeKeywords.builder()
                        .finalFeedback("feedback")
                        .build();

        when(restTemplate.postForObject(
                eq("http://localhost:8000/api/ai-coaching/youtube-keywords"),
                eq(request),
                eq(FastApiOpenAiDto.ResponseYoutubeKeywords.class)
        )).thenThrow(new RestClientException("FastAPI connection failed"));

        // when & then
        assertThatThrownBy(() -> fastApiOpenAiClient.createYoutubeKeywords(request))
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