package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kr.co.mapspring.global.exception.place.MissionSessionNotFoundException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserMissionStartDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.LearningSessionLevel;
import kr.co.mapspring.place.enums.LearningSessionStatus;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.MissionSessionRepository;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.SessionMessageRepository;
import kr.co.mapspring.place.service.impl.UserPlaceLearningServiceImpl;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserPlaceLearningServiceTest {

    @InjectMocks
    private UserPlaceLearningServiceImpl userPlaceLearningService;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LearningSessionRepository learningSessionRepository;
    
    @Mock
    private MissionSessionRepository missionSessionRepository;
    
    @Mock SessionMessageRepository sessionMessageRepository;

    @Test
    @DisplayName("마커 상세 정보 조회 성공")
    void 마커_상세_정보_조회_성공() {
        // given
        Long placeId = 1L;
        Long scenarioId = 2L;

        Place place = mock(Place.class);
        Scenario scenario = mock(Scenario.class);
        Mission mission1 = mock(Mission.class);
        Mission mission2 = mock(Mission.class);

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.of(place));

        when(place.getPlaceName()).thenReturn("광화문역");
        when(place.getPlaceDescription()).thenReturn("서울 중심부에 위치한 지하철역");
        when(place.getRegion()).thenReturn(null);
        when(place.getScenario()).thenReturn(scenario);

        when(scenario.getScenarioId()).thenReturn(scenarioId);
        when(scenario.getCategory()).thenReturn("TRANSPORT");
        when(scenario.getScenarioDescription()).thenReturn("지하철역에서 길을 묻는 상황");

        when(mission1.getMissionTitle()).thenReturn("출구 묻기");
        when(mission1.getMissionDescription()).thenReturn("원하는 출구를 영어로 묻는다.");

        when(mission2.getMissionTitle()).thenReturn("환승 묻기");
        when(mission2.getMissionDescription()).thenReturn("환승 방법을 영어로 묻는다.");

        when(missionRepository.findByScenario_ScenarioId(scenarioId))
                .thenReturn(List.of(mission1, mission2));

        // when
        UserReadPlaceDto.ResponseRead response =
                userPlaceLearningService.markerDetail(placeId);

        // then
        assertEquals("광화문역", response.getPlaceName());
        assertEquals("서울 중심부에 위치한 지하철역", response.getPlaceDescription());
        assertEquals("TRANSPORT", response.getScenarioCategory());
        assertEquals("지하철역에서 길을 묻는 상황", response.getScenarioDescription());
        assertEquals(2, response.getMission().size());
        assertEquals("출구 묻기", response.getMission().get(0).getMissionTitle());

        verify(placeRepository, times(1)).findById(placeId);
        verify(missionRepository, times(1)).findByScenario_ScenarioId(scenarioId);
    }

    @Test
    @DisplayName("마커 상세 정보 조회 실패 존재하지 않는 장소")
    void 마커_상세_정보_조회_실패_존재하지_않는_장소() {
        // given
        Long placeId = 999L;

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(PlaceNotFoundException.class,
                () -> userPlaceLearningService.markerDetail(placeId));

        verify(placeRepository, times(1)).findById(placeId);
        verify(missionRepository, never()).findByScenario_ScenarioId(any());
    }

    @Test
    @DisplayName("학습 세션 생성 성공")
    void 학습_세션_생성_성공() {
        // given
        Long placeId = 1L;
        Long userId = 10L;
        Long scenarioId = 20L;

        UserCreateLearningSessionDto.RequestCreate request =
                UserCreateLearningSessionDto.RequestCreate.builder()
                        .userId(userId)
                        .level(LearningSessionLevel.BEGINNER)
                        .build();

        User user = mock(User.class);

        Scenario scenario = mock(Scenario.class);
        when(scenario.getScenarioId()).thenReturn(scenarioId);

        Place place = mock(Place.class);
        when(place.getScenario()).thenReturn(scenario);

        Mission mission1 = mock(Mission.class);
        Mission mission2 = mock(Mission.class);
        List<Mission> missionList = List.of(mission1, mission2);

        LearningSession savedSession =
                LearningSession.create(place, user, LearningSessionLevel.BEGINNER);

        ReflectionTestUtils.setField(savedSession, "sessionId", 100L);

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.of(place));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(learningSessionRepository.save(any(LearningSession.class)))
                .thenReturn(savedSession);

        when(missionRepository.findByScenario_ScenarioId(scenarioId))
                .thenReturn(missionList);

        // when
        UserCreateLearningSessionDto.ResponseCreate response =
                userPlaceLearningService.learningStart(placeId, request);

        // then
        assertEquals(100L, response.getLearningSessionId());
        assertEquals(LearningSessionStatus.RUNNING, response.getLearningSessionStatus());

        verify(placeRepository, times(1)).findById(placeId);
        verify(userRepository, times(1)).findById(userId);
        verify(learningSessionRepository, times(1)).save(any(LearningSession.class));

        verify(missionRepository, times(1)).findByScenario_ScenarioId(scenarioId);
        verify(missionSessionRepository, times(1)).saveAll(anyList());
    }
    
    @Test
    @DisplayName("학습 세션 생성 실패 - 존재하지 않는 장소")
    void 학습_세션_생성_실패_존재하지_않는_장소() {
        // given
        Long placeId = 999L;

        UserCreateLearningSessionDto.RequestCreate request =
                UserCreateLearningSessionDto.RequestCreate.builder()
                        .userId(10L)
                        .level(LearningSessionLevel.BEGINNER)
                        .build();

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(PlaceNotFoundException.class,
                () -> userPlaceLearningService.learningStart(placeId, request));

        verify(placeRepository, times(1)).findById(placeId);
        verify(userRepository, never()).findById(any());
        verify(learningSessionRepository, never()).save(any());
        verify(missionRepository, never()).findByScenario_ScenarioId(any());
        verify(missionSessionRepository, never()).saveAll(anyList());
    }
    
    @Test
    @DisplayName("학습 세션 생성 실패 - 존재하지 않는 사용자")
    void 학습_세션_생성_실패_존재하지_않는_사용자() {
        // given
        Long placeId = 1L;
        Long userId = 999L;

        Place place = mock(Place.class);

        UserCreateLearningSessionDto.RequestCreate request =
                UserCreateLearningSessionDto.RequestCreate.builder()
                        .userId(userId)
                        .level(LearningSessionLevel.BEGINNER)
                        .build();

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.of(place));

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () -> userPlaceLearningService.learningStart(placeId, request));

        verify(placeRepository, times(1)).findById(placeId);
        verify(userRepository, times(1)).findById(userId);
        verify(learningSessionRepository, never()).save(any());
        verify(missionRepository, never()).findByScenario_ScenarioId(any());
        verify(missionSessionRepository, never()).saveAll(anyList());
    }
    
    @Test
    @DisplayName("미션 시작 성공")
    void 미션_시작_성공() {
        // given
        Long sessionId = 100L;
        Long missionId = 1L;

        LearningSession learningSession = mock(LearningSession.class);
        Mission mission = mock(Mission.class);
        MissionSession missionSession = mock(MissionSession.class);

        when(missionSession.getLearningSession()).thenReturn(learningSession);
        when(missionSession.getMission()).thenReturn(mission);

        when(missionSessionRepository.findByLearningSession_SessionIdAndMission_MissionId(sessionId, missionId))
                .thenReturn(Optional.of(missionSession));

        when(sessionMessageRepository.save(any(SessionMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserMissionStartDto.ResponseMissionStart response =
                userPlaceLearningService.missionStart(sessionId, missionId);

        // then
        assertNotNull(response);

        verify(missionSessionRepository, times(1))
                .findByLearningSession_SessionIdAndMission_MissionId(sessionId, missionId);

        verify(missionSession, times(1)).start();

        verify(sessionMessageRepository, times(1))
                .save(any(SessionMessage.class));

        verify(missionSession, times(1)).getLearningSession();
        verify(missionSession, times(1)).getMission();
    }
    
    @Test
    @DisplayName("미션 시작 실패 - 존재하지 않는 미션 세션")
    void 미션_시작_실패_존재하지_않는_미션_세션() {
        // given
        Long sessionId = 999L;
        Long missionId = 999L;

        when(missionSessionRepository.findByLearningSession_SessionIdAndMission_MissionId(sessionId, missionId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(MissionSessionNotFoundException.class,
                () -> userPlaceLearningService.missionStart(sessionId, missionId));

        verify(missionSessionRepository, times(1))
                .findByLearningSession_SessionIdAndMission_MissionId(sessionId, missionId);

        verify(sessionMessageRepository, never())
                .save(any(SessionMessage.class));
    }
}
