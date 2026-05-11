package kr.co.mapspring.place.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.global.exception.place.ChatLimitExceededException;
import kr.co.mapspring.global.exception.place.MissionSessionNotFoundException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.client.FastApiClient;
import kr.co.mapspring.place.dto.UserChatDto;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserLearningProgressDto;
import kr.co.mapspring.place.dto.UserMissionCompleteDto;
import kr.co.mapspring.place.dto.UserMissionStartDto;
import kr.co.mapspring.place.dto.UserPlaceListDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.dto.UserRegionListDto;
import kr.co.mapspring.place.dto.fastapi.FastApiChatDto;
import kr.co.mapspring.place.dto.fastapi.FastApiEvaluationDto;
import kr.co.mapspring.place.dto.fastapi.FastApiMissionStartDto;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.MissionSession;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.entity.SessionEvaluation;
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.MissionSessionStatus;
import kr.co.mapspring.place.enums.SessionMessageRole;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.MissionSessionRepository;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.repository.SessionEvaluationRepository;
import kr.co.mapspring.place.repository.SessionMessageRepository;
import kr.co.mapspring.place.service.UserPlaceLearningService;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPlaceLearningServiceImpl implements UserPlaceLearningService {
	
	private final PlaceRepository placeRepository;
	private final MissionRepository missionRepository;
	private final UserRepository userRepository;
	private final LearningSessionRepository learningSessionRepository;
	private final MissionSessionRepository missionSessionRepository;
	private final SessionMessageRepository sessionMessageRepository;
	private final SessionEvaluationRepository sessionEvaluationRepository;
	private final RegionRepository regionRepository;
	private final FastApiClient fastApiClient;
	
	// 마커 조회
	@Override
	@Transactional(readOnly = true)
	public List<UserPlaceListDto.ResponseList> readPlaceMarkers() {

		List<Place> places = placeRepository.findAll();
		
		return places.stream()
				.map(UserPlaceListDto.ResponseList::from)
				.toList();
	}
	
	// 마커 상세 정보 조회
	@Override
	@Transactional(readOnly = true)
	public UserReadPlaceDto.ResponseRead markerDetail(Long placeId) {
		
		Place placeDetail = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		Long scenarioId = placeDetail.getScenario().getScenarioId();
		
		List<Mission> missionList = missionRepository.findByScenario_ScenarioId(scenarioId);
		
		return UserReadPlaceDto.ResponseRead.from(placeDetail, missionList);
	}
	
	// 학습하기 클릭 시 학습 세션 생성
	@Override
	@Transactional
	public UserCreateLearningSessionDto.ResponseCreate learningStart(
	        Long placeId,
	        UserCreateLearningSessionDto.RequestCreate request
	) {

	    Long userId = request.getUserId();

	    Place place = placeRepository.findById(placeId)
	            .orElseThrow(PlaceNotFoundException::new);

	    User user = userRepository.findById(userId)
	            .orElseThrow(UserNotFoundException::new);

	    Optional<LearningSession> existingSession =
	            learningSessionRepository.findByUser_UserIdAndPlace_PlaceId(userId, placeId);

	    if (existingSession.isPresent()) {
	        return UserCreateLearningSessionDto.ResponseCreate.from(existingSession.get());
	    }

	    LearningSession learningSession =
	            LearningSession.create(place, user, request.getLevel());

	    LearningSession savedLearningSession =
	            learningSessionRepository.save(learningSession);

	    Long scenarioId = place.getScenario().getScenarioId();

	    List<Mission> missionList =
	            missionRepository.findByScenario_ScenarioId(scenarioId);

	    List<MissionSession> missionSessionList = missionList.stream()
	            .map(mission -> MissionSession.create(savedLearningSession, mission))
	            .toList();

	    missionSessionRepository.saveAll(missionSessionList);

	    String initialMessage =
	            place.getPlaceName()
	                    + " 학습 세션이 시작되었습니다. 이제 미션을 선택해서 대화를 시작해보세요.";

	    SessionMessage startMessage = SessionMessage.create(
	            savedLearningSession,
	            null,
	            initialMessage,
	            SessionMessageRole.ASSISTANT
	    );

	    sessionMessageRepository.save(startMessage);

	    return UserCreateLearningSessionDto.ResponseCreate.from(savedLearningSession);
	}
	
	// 미션 시작
	@Override
	@Transactional
	public UserMissionStartDto.ResponseMissionStart missionStart(
	        Long userId,
	        Long sessionId,
	        Long missionId
	) {
	    LearningSession learningSession =
	            learningSessionRepository.findBySessionIdAndUser_UserId(sessionId, userId)
	                    .orElseThrow(LearningSessionNotFoundException::new);

	    MissionSession missionSession =
	            missionSessionRepository
	                    .findByLearningSession_SessionIdAndMission_MissionId(
	                            learningSession.getSessionId(),
	                            missionId
	                    )
	                    .orElseThrow(MissionSessionNotFoundException::new);

	    missionSession.start();

	    Mission mission = missionSession.getMission();
	    Scenario scenario = learningSession.getPlace().getScenario();

	    FastApiMissionStartDto.RequestMissionStart fastApiRequest =
	            FastApiMissionStartDto.RequestMissionStart.builder()
	                    .level(learningSession.getLevel().name())
	                    .scenarioPrompt(scenario.getPrompt())
	                    .scenarioCategory(scenario.getCategory())
	                    .missionTitle(mission.getMissionTitle())
	                    .missionDescription(mission.getMissionDescription())
	                    .build();

	    FastApiMissionStartDto.ResponseMissionStart fastApiResponse =
	            fastApiClient.missionStart(fastApiRequest);

	    String aiMessage = fastApiResponse.getAiMessage();

	    SessionMessage aiStartMessage = SessionMessage.create(
	            learningSession,
	            missionSession,
	            aiMessage,
	            SessionMessageRole.ASSISTANT
	    );

	    sessionMessageRepository.save(aiStartMessage);

	    return UserMissionStartDto.ResponseMissionStart.of(
	            mission,
	            missionSession,
	            aiMessage
	    );
	}
	
	// 채팅 기능
	@Override
	@Transactional
	public UserChatDto.ResponseChat chat(UserChatDto.RequestChat request) {

	    LearningSession learningSession =
	            learningSessionRepository
	                    .findBySessionIdAndUser_UserId(
	                            request.getSessionId(),
	                            request.getUserId()
	                    )
	                    .orElseThrow(LearningSessionNotFoundException::new);

	    MissionSession runningMissionSession = missionSessionRepository
	            .findByLearningSession_SessionIdAndMissionStatus(
	                    learningSession.getSessionId(),
	                    MissionSessionStatus.RUNNING
	            )
	            .orElseThrow(MissionSessionNotFoundException::new);

	    long userMessageCount = sessionMessageRepository
	            .countByMissionSession_MissionSessionIdAndRole(
	                    runningMissionSession.getMissionSessionId(),
	                    SessionMessageRole.USER
	            );

	    if (userMessageCount >= 10) {
	        throw new ChatLimitExceededException();
	    }

	    SessionMessage userMessage = SessionMessage.create(
	            learningSession,
	            runningMissionSession,
	            request.getMessage(),
	            SessionMessageRole.USER
	    );

	    sessionMessageRepository.save(userMessage);

	    List<SessionMessage> messageHistory =
	            sessionMessageRepository
	                    .findByMissionSession_MissionSessionIdOrderByCreatedAtAsc(
	                            runningMissionSession.getMissionSessionId()
	                    );

	    Mission mission = runningMissionSession.getMission();
	    Scenario scenario = learningSession.getPlace().getScenario();

	    FastApiChatDto.RequestChat fastApiRequest = FastApiChatDto.RequestChat.builder()
	            .userMessage(request.getMessage())
	            .messages(messageHistory.stream()
	                    .map(message -> FastApiChatDto.MessageHistory.builder()
	                            .role(message.getRole().name().toLowerCase())
	                            .message(message.getMessage())
	                            .build())
	                    .toList())
	            .missionTitle(mission.getMissionTitle())
	            .missionDescription(mission.getMissionDescription())
	            .scenarioPrompt(scenario.getPrompt())
	            .scenarioCategory(scenario.getCategory())
	            .build();

	    FastApiChatDto.ResponseChat fastApiResponse =
	            fastApiClient.chat(fastApiRequest);

	    String aiMessage = fastApiResponse.getAiMessage();

	    SessionMessage assistantMessage = SessionMessage.create(
	            learningSession,
	            runningMissionSession,
	            aiMessage,
	            SessionMessageRole.ASSISTANT
	    );

	    sessionMessageRepository.save(assistantMessage);

	    return UserChatDto.ResponseChat.builder()
	            .aiMessage(aiMessage)
	            .build();
	}
	
	// 미션 완료 기능
	@Override
	@Transactional
	public UserMissionCompleteDto.ResponseComplete missionComplete(
	        Long userId,
	        Long sessionId,
	        Long missionId
	) {

	    LearningSession learningSession =
	            learningSessionRepository
	                    .findBySessionIdAndUser_UserId(sessionId, userId)
	                    .orElseThrow(LearningSessionNotFoundException::new);

	    MissionSession missionSession = missionSessionRepository
	            .findByLearningSession_SessionIdAndMission_MissionId(
	                    learningSession.getSessionId(),
	                    missionId
	            )
	            .orElseThrow(MissionSessionNotFoundException::new);

	    missionSession.complete();

	    boolean hasNotCompletedMission =
	            missionSessionRepository.existsByLearningSession_SessionIdAndMissionStatusNot(
	                    learningSession.getSessionId(),
	                    MissionSessionStatus.COMPLETED
	            );

	    String evaluation = null;

	    if (!hasNotCompletedMission) {

	        learningSession.complete();

	        List<SessionMessage> userMessageHistory = sessionMessageRepository
	                .findBySession_SessionIdOrderByCreatedAtAsc(
	                        learningSession.getSessionId()
	                )
	                .stream()
	                .filter(message -> message.getRole() == SessionMessageRole.USER)
	                .toList();

	        Scenario scenario = learningSession
	                .getPlace()
	                .getScenario();

	        FastApiEvaluationDto.RequestEvaluation fastApiRequest =
	                FastApiEvaluationDto.RequestEvaluation.builder()
	                        .scenarioPrompt(scenario.getPrompt())
	                        .scenarioCategory(scenario.getCategory())
	                        .messages(userMessageHistory.stream()
	                                .map(message -> FastApiEvaluationDto.MessageHistory.builder()
	                                        .role(message.getRole().name().toLowerCase())
	                                        .message(message.getMessage())
	                                        .build())
	                                .toList())
	                        .build();

	        FastApiEvaluationDto.ResponseEvaluation fastApiResponse =
	                fastApiClient.evaluate(fastApiRequest);

	        evaluation = fastApiResponse.getEvaluation();

	        SessionEvaluation sessionEvaluation =
	                SessionEvaluation.create(learningSession, evaluation);

	        sessionEvaluationRepository.save(sessionEvaluation);
	    }

	    return UserMissionCompleteDto.ResponseComplete.of(
	            missionSession,
	            learningSession,
	            evaluation
	    );
	}
	
	// 사용자 진행 사항 유지
	@Override
	@Transactional(readOnly = true)
	public List<UserLearningProgressDto.Response> readMyProgress(Long userId) {

	    List<LearningSession> learningSessions =
	            learningSessionRepository.findByUser_UserId(userId);

	    return learningSessions.stream()
	            .map(learningSession -> {
	                Long sessionId = learningSession.getSessionId();

	                List<MissionSession> missionSessions =
	                        missionSessionRepository.findByLearningSession_SessionId(sessionId);

	                List<SessionMessage> sessionMessages =
	                        sessionMessageRepository.findBySession_SessionIdOrderByCreatedAtAsc(sessionId);

	                SessionEvaluation sessionEvaluation =
	                        sessionEvaluationRepository
	                                .findBySession_SessionId(sessionId)
	                                .orElse(null);

	                List<SessionMessage> mergedMessages =
	                        new ArrayList<>(sessionMessages);

	                if (sessionEvaluation != null) {
	                    SessionMessage evaluationMessage = SessionMessage.create(
	                            learningSession,
	                            null,
	                            sessionEvaluation.getEvaluation(),
	                            SessionMessageRole.ASSISTANT
	                    );

	                    mergedMessages.add(evaluationMessage);
	                }

	                return UserLearningProgressDto.Response.from(
	                        learningSession,
	                        missionSessions,
	                        mergedMessages
	                );
	            })
	            .toList();
	}
	
	
	@Override
	@Transactional(readOnly = true)
    public List<UserRegionListDto.ResponseList> readRegionList() {

        return regionRepository.findAll()
                .stream()
                .map(UserRegionListDto.ResponseList::from)
                .toList();
    }

}
