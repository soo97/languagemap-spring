package kr.co.mapspring.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.ai.dto.ContentDto;
import kr.co.mapspring.ai.dto.FastApiYoutubeDto;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.entity.Content;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.repository.ContentRepository;
import kr.co.mapspring.ai.service.impl.ContentServiceImpl;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @InjectMocks
    private ContentServiceImpl contentService;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private CoachingSessionRepository coachingSessionRepository;

    private CoachingSession coachingSession;
    private Content content;

    @BeforeEach
    void setUp() throws Exception {
        LearningSession learningSession = createInstance(LearningSession.class);
        setField(learningSession, "sessionId", 10L);

        coachingSession = createInstance(CoachingSession.class);
        setField(coachingSession, "coachingSessionId", 100L);
        setField(coachingSession, "learningSession", learningSession);
        setField(coachingSession, "coachingSessionStatus", CoachingSessionStatus.COMPLETED);
        setField(coachingSession, "selectedOption", "WORD");
        setField(coachingSession, "currentTurnOrder", 3);

        content = createInstance(Content.class);
        setField(content, "contentId", 1L);
        setField(content, "coachingSession", coachingSession);
        setField(content, "keyword", "cafe English ordering politely");
        setField(content, "videoTitle", "How to Order Coffee in English");
        setField(content, "channelTitle", "EnglishClass101");
        setField(content, "thumbnailUrl", "https://img.youtube.com/test.jpg");
        setField(content, "videoUrl", "https://www.youtube.com/watch?v=test");
        setField(content, "videoSummary", "카페에서 정중하게 주문하는 표현을 연습하기 좋은 영상입니다.");
        setField(content, "createdAt", LocalDateTime.of(2026, 5, 4, 10, 0));
    }

    @Test
    @DisplayName("유튜브 추천 콘텐츠 저장 성공")
    void 유튜브_추천_콘텐츠_저장_성공() {
        FastApiYoutubeDto.YoutubeVideoItem video = createYoutubeVideoItem();

        when(coachingSessionRepository.findById(100L))
                .thenReturn(Optional.of(coachingSession));

        contentService.saveAll(
                100L,
                "cafe English ordering politely",
                List.of(video)
        );

        verify(contentRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("유튜브 추천 콘텐츠 저장 실패 - 코칭 세션이 존재하지 않는다")
    void 유튜브_추천_콘텐츠_저장_실패_세션없음() {
        FastApiYoutubeDto.YoutubeVideoItem video = createYoutubeVideoItem();

        when(coachingSessionRepository.findById(999L))
                .thenReturn(Optional.empty());
 
        assertThatThrownBy(() -> contentService.saveAll(
                999L,
                "cafe English ordering politely",
                List.of(video)
        ))
                .isInstanceOf(CoachingSessionNotFoundException.class)
                .hasMessage("코칭 세션을 찾을 수 없습니다.");

        verify(contentRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("유튜브 추천 콘텐츠 목록 조회 성공")
    void 유튜브_추천_콘텐츠_목록_조회_성공() {
        when(contentRepository.findByCoachingSession_CoachingSessionId(100L))
                .thenReturn(List.of(content));

        ContentDto.ResponseGetContents response = contentService.getContents(100L);

        assertThat(response.getContents()).hasSize(1);
        assertThat(response.getContents().get(0).getKeyword())
                .isEqualTo("cafe English ordering politely");
        assertThat(response.getContents().get(0).getVideoTitle())
                .isEqualTo("How to Order Coffee in English");
        assertThat(response.getContents().get(0).getChannelTitle())
                .isEqualTo("EnglishClass101");
        assertThat(response.getContents().get(0).getVideoSummary())
                .isEqualTo("카페에서 정중하게 주문하는 표현을 연습하기 좋은 영상입니다.");
    }

    private FastApiYoutubeDto.YoutubeVideoItem createYoutubeVideoItem() {
        FastApiYoutubeDto.YoutubeVideoItem video = new FastApiYoutubeDto.YoutubeVideoItem();

        setFieldUnchecked(video, "title", "How to Order Coffee in English");
        setFieldUnchecked(video, "channelTitle", "EnglishClass101");
        setFieldUnchecked(video, "thumbnailUrl", "https://img.youtube.com/test.jpg");
        setFieldUnchecked(video, "videoUrl", "https://www.youtube.com/watch?v=test");
        setFieldUnchecked(video, "description", "Learn cafe English.");
        setFieldUnchecked(video, "summary", "카페에서 정중하게 주문하는 표현을 연습하기 좋은 영상입니다.");

        return video;
    }

    private <T> T createInstance(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private void setFieldUnchecked(Object target, String fieldName, Object value) {
        try {
            setField(target, fieldName, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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