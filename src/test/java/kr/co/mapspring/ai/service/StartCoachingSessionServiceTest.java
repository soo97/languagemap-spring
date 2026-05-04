package kr.co.mapspring.ai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.impl.StartCoachingSessionServiceImpl;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.LearningSessionRepository;

@ExtendWith(MockitoExtension.class)
class StartCoachingSessionServiceTest {

    @InjectMocks
    private StartCoachingSessionServiceImpl startCoachingSessionService;

    @Mock
    private CoachingSessionRepository coachingSessionRepository;

    @Mock
    private LearningSessionRepository learningSessionRepository;

    @Test
    @DisplayName("코칭 세션 시작 성공 - RUNNING 세션이 없으면 새로 생성한다")
    void 코칭_세션_시작_성공_RUNNING_세션이_없으면_새로_생성() throws Exception {
        // given
        StartCoachingSessionDto.RequestStartCoachingSession request =
                StartCoachingSessionDto.RequestStartCoachingSession.builder()
                        .sessionId(10L)
                        .optionType("WORD")
                        .build();

        LearningSession learningSession = createLearningSession(10L);
        CoachingSession coachingSession = CoachingSession.start(learningSession, "WORD");
        setField(coachingSession, "coachingSessionId", 100L);

        when(learningSessionRepository.findBySessionId(10L))
                .thenReturn(Optional.of(learningSession));

        when(coachingSessionRepository.findByLearningSession_SessionIdAndCoachingSessionStatus(
                10L, CoachingSessionStatus.RUNNING))
                .thenReturn(Optional.empty());

        when(coachingSessionRepository.save(any(CoachingSession.class)))
                .thenReturn(coachingSession);

        // when
        StartCoachingSessionDto.ResponseStartCoachingSession response =
                startCoachingSessionService.startCoachingSession(request);

        // then
        assertEquals(100L, response.getCoachingSessionId());
        assertEquals(10L, response.getSessionId());
        assertEquals(CoachingSessionStatus.RUNNING.name(), response.getCoachingSessionStatus());
        assertEquals("WORD", response.getSelectedOption());
        assertEquals(0, response.getCurrentTurnOrder());
        verify(coachingSessionRepository).save(any(CoachingSession.class));
    }

    @Test
    @DisplayName("코칭 세션 시작 성공 - RUNNING 세션이 이미 있으면 기존 세션을 반환한다")
    void 코칭_세션_시작_성공_RUNNING_세션이_이미_있으면_기존_세션_반환() throws Exception {
        // given
        StartCoachingSessionDto.RequestStartCoachingSession request =
                StartCoachingSessionDto.RequestStartCoachingSession.builder()
                        .sessionId(10L)
                        .optionType("GRAMMAR")
                        .build();

        LearningSession learningSession = createLearningSession(10L);
        CoachingSession existingCoachingSession = CoachingSession.start(learningSession, "GRAMMAR");
        setField(existingCoachingSession, "coachingSessionId", 200L);

        when(learningSessionRepository.findBySessionId(10L))
                .thenReturn(Optional.of(learningSession));

        when(coachingSessionRepository.findByLearningSession_SessionIdAndCoachingSessionStatus(
                10L, CoachingSessionStatus.RUNNING))
                .thenReturn(Optional.of(existingCoachingSession));

        // when
        StartCoachingSessionDto.ResponseStartCoachingSession response =
                startCoachingSessionService.startCoachingSession(request);

        // then
        assertEquals(200L, response.getCoachingSessionId());
        assertEquals(10L, response.getSessionId());
        assertEquals(CoachingSessionStatus.RUNNING.name(), response.getCoachingSessionStatus());
        assertEquals("GRAMMAR", response.getSelectedOption());
        assertEquals(0, response.getCurrentTurnOrder());
        verify(coachingSessionRepository, never()).save(any(CoachingSession.class));
    }

    @Test
    @DisplayName("코칭 세션 시작 실패 - 존재하지 않는 학습 세션")
    void 코칭_세션_시작_실패_존재하지_않는_학습_세션() {
        // given
        StartCoachingSessionDto.RequestStartCoachingSession request =
                StartCoachingSessionDto.RequestStartCoachingSession.builder()
                        .sessionId(999L)
                        .optionType("DIALOGUE")
                        .build();

        when(learningSessionRepository.findBySessionId(999L))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(LearningSessionNotFoundException.class,
                () -> startCoachingSessionService.startCoachingSession(request));

        verify(coachingSessionRepository, never()).save(any(CoachingSession.class));
    }

    private LearningSession createLearningSession(Long sessionId) throws Exception {
        Region region = createInstance(Region.class);
        setField(region, "regionId", 1L);
        setField(region, "country", "Australia");
        setField(region, "city", "Sydney");

        Scenario scenario = createInstance(Scenario.class);
        setField(scenario, "scenarioId", 2L);
        setField(scenario, "prompt", "coffee ordering prompt");
        setField(scenario, "scenarioDescription", "Cafe ordering");
        setField(scenario, "completeExp", 10);
        setField(scenario, "category", "CAFE");

        Place place = createInstance(Place.class);
        setField(place, "placeId", 3L);
        setField(place, "googlePlaceId", "google-place-1");
        setField(place, "placeName", "Cafe Stage 888");
        setField(place, "placeDescription", "Sydney CBD cafe");
        setField(place, "latitude", new BigDecimal("37.12345678"));
        setField(place, "longitude", new BigDecimal("127.12345678"));
        setField(place, "region", region);
        setField(place, "scenario", scenario);

        LearningSession learningSession = createInstance(LearningSession.class);
        setField(learningSession, "sessionId", sessionId);
        setField(learningSession, "place", place);

        return learningSession;
    }

    private <T> T createInstance(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
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