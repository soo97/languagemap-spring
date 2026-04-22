package kr.co.mapspring.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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

import kr.co.mapspring.ai.dto.CoachingEntryDto;
import kr.co.mapspring.ai.serviceImpl.CoachingEntryServiceImpl;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.entity.SessionEvaluation;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.ScenarioLevel;
import kr.co.mapspring.place.enums.SessionMessageRole;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.SessionEvaluationRepository;
import kr.co.mapspring.place.repository.SessionMessageRepository;

@ExtendWith(MockitoExtension.class)
class CoachingEntryServiceTest {

    @Mock
    private LearningSessionRepository learningSessionRepository;

    @Mock
    private SessionMessageRepository sessionMessageRepository;

    @Mock
    private SessionEvaluationRepository sessionEvaluationRepository;

    @InjectMocks
    private CoachingEntryServiceImpl coachingEntryService;

    private LearningSession learningSession;
    private Place place;
    private Region region;
    private Scenario scenario;
    private SessionMessage userMessage;
    private SessionMessage assistantMessage;
    private SessionEvaluation sessionEvaluation;

    @BeforeEach
    void setUp() throws Exception {
        region = Region.builder()
                .regionId(1L)
                .country("Australia")
                .city("Sydney")
                .build();

        scenario = Scenario.builder()
                .scenarioId(2L)
                .prompt("coffee ordering prompt")
                .scenariosDescription("Cafe ordering")
                .completeExp(10)
                .level(ScenarioLevel.BEGINNER)
                .category("CAFE")
                .build();

        place = Place.builder()
                .placeId(3L)
                .googlePlaceId("google-place-1")
                .placeName("Cafe Stage 888")
                .placeDescription("Sydney CBD cafe")
                .latitude(new BigDecimal("37.12345678"))
                .longitude(new BigDecimal("127.12345678"))
                .region(region)
                .scenario(scenario)
                .build();

        learningSession = new LearningSession();
        setField(learningSession, "sessionId", 10L);
        setField(learningSession, "place", place);

        userMessage = new SessionMessage();
        setField(userMessage, "messageId", 100L);
        setField(userMessage, "session", learningSession);
        setField(userMessage, "role", SessionMessageRole.USER);
        setField(userMessage, "message", "I would like a latte.");
        setField(userMessage, "createdAt", LocalDateTime.of(2026, 4, 22, 10, 0));

        assistantMessage = new SessionMessage();
        setField(assistantMessage, "messageId", 101L);
        setField(assistantMessage, "session", learningSession);
        setField(assistantMessage, "role", SessionMessageRole.ASSISTANT);
        setField(assistantMessage, "message", "Sure. Is that for here or to go?");
        setField(assistantMessage, "createdAt", LocalDateTime.of(2026, 4, 22, 10, 1));

        sessionEvaluation = new SessionEvaluation();
        setField(sessionEvaluation, "evaluationId", 1000L);
        setField(sessionEvaluation, "session", learningSession);
        setField(sessionEvaluation, "evaluation", "발음 보통, 표현 좋음, 속도 개선 필요");
    }

    @Test
    @DisplayName("coaching entry 데이터를 정상 조회한다")
    void getCoachingEntryData_success() {
        // given
        Long sessionId = 10L;

        when(learningSessionRepository.findBySessionId(sessionId))
                .thenReturn(Optional.of(learningSession));
        when(sessionMessageRepository.findBySession_SessionIdOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of(userMessage, assistantMessage));
        when(sessionEvaluationRepository.findBySession_SessionId(sessionId))
                .thenReturn(Optional.of(sessionEvaluation));

        // when
        CoachingEntryDto.Response response = coachingEntryService.getCoachingEntryData(sessionId);

        // then
        assertThat(response.getSessionId()).isEqualTo(10L);
        assertThat(response.getPlaceId()).isEqualTo(3L);
        assertThat(response.getPlaceName()).isEqualTo("Cafe Stage 888");
        assertThat(response.getCountry()).isEqualTo("Australia");
        assertThat(response.getCity()).isEqualTo("Sydney");
        assertThat(response.getPlaceDescription()).isEqualTo("Sydney CBD cafe");
        assertThat(response.getEvaluation()).isEqualTo("발음 보통, 표현 좋음, 속도 개선 필요");
        assertThat(response.getSessionMessages()).hasSize(2);
        assertThat(response.getSessionMessages().get(0).getRole()).isEqualTo(SessionMessageRole.USER);
        assertThat(response.getSessionMessages().get(0).getMessage()).isEqualTo("I would like a latte.");
        assertThat(response.getSessionMessages().get(1).getRole()).isEqualTo(SessionMessageRole.ASSISTANT);
        assertThat(response.getSessionMessages().get(1).getMessage()).isEqualTo("Sure. Is that for here or to go?");
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