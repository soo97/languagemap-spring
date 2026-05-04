package kr.co.mapspring.place.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.MissionSessionNotFoundException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.client.FastApiClient;
import kr.co.mapspring.place.dto.UserChatDto;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserMissionCompleteDto;
import kr.co.mapspring.place.dto.UserMissionStartDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
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
	private final FastApiClient fastApiClient;
	
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
	public UserCreateLearningSessionDto.ResponseCreate learningStart(Long placeId, UserCreateLearningSessionDto.RequestCreate request) {
		
		// 학습 세션 생성
		Place place = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		Long userId = request.getUserId();
		
		User user = userRepository.findById(userId)
				.orElseThrow(UserNotFoundException::new);
		
		LearningSession learningSession = LearningSession.create(place, user, request.getLevel());
		
		LearningSession saveLearningSession = learningSessionRepository.save(learningSession);
		
		// 미션 세션 생성 미션 엔티티
		Long scenarioId = place.getScenario().getScenarioId();
		
		List<Mission> missionList = missionRepository.findByScenario_ScenarioId(scenarioId);
		
		List<MissionSession> missionSessionList = missionList.stream()
				.map(mission -> MissionSession.create(saveLearningSession, mission))
				.toList();
		
		missionSessionRepository.saveAll(missionSessionList);
		
		
		return UserCreateLearningSessionDto.ResponseCreate.from(saveLearningSession);
	}
	
	// 미션 시작
		@Override
		@Transactional
		public UserMissionStartDto.ResponseMissionStart missionStart(Long sessionId, Long missionId) {
			
			MissionSession missionSession = missionSessionRepository.findByLearningSession_SessionIdAndMission_MissionId(sessionId, missionId)
					.orElseThrow(MissionSessionNotFoundException::new);
			
			missionSession.start();
			
			LearningSession learningSession = missionSession.getLearningSession();
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

		    FastApiMissionStartDto.ResponseMissionStart fastApiResponse = fastApiClient.missionStart(fastApiRequest);
		     
			String aiMessage = fastApiResponse.getAiMessage();
			
			SessionMessage aiStartMessage = SessionMessage.create(
			        missionSession.getLearningSession(),
			        missionSession,
			        aiMessage,
			        SessionMessageRole.ASSISTANT
			);
			
			sessionMessageRepository.save(aiStartMessage);
			
			UserMissionStartDto.ResponseMissionStart response = UserMissionStartDto.ResponseMissionStart.of(mission, missionSession, aiMessage);
			
			return response;
		}
	
	// 채팅 기능
	@Override
	@Transactional
	public UserChatDto.ResponseChat chat(UserChatDto.RequestChat request) {

	    MissionSession runningMissionSession = missionSessionRepository
	            .findByLearningSession_SessionIdAndMissionStatus(
	                    request.getSessionId(),
	                    MissionSessionStatus.RUNNING
	            )
	            .orElseThrow(MissionSessionNotFoundException::new);

	    SessionMessage userMessage = SessionMessage.create(
	            runningMissionSession.getLearningSession(),
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
	    Scenario scenario = runningMissionSession.getLearningSession().getPlace().getScenario();

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

	    // 5. FastAPI 호출
	    FastApiChatDto.ResponseChat fastApiResponse = fastApiClient.chat(fastApiRequest);
	    
	    String aiMessage = fastApiResponse.getAiMessage();

	    SessionMessage assistantMessage = SessionMessage.create(
	            runningMissionSession.getLearningSession(),
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
	public UserMissionCompleteDto.ResponseComplete missionComplete(Long sessionId, Long missionId) {

	    MissionSession missionSession = missionSessionRepository
	            .findByLearningSession_SessionIdAndMission_MissionId(sessionId, missionId)
	            .orElseThrow(MissionSessionNotFoundException::new);

	    missionSession.complete();

	    LearningSession learningSession = missionSession.getLearningSession();

	    boolean hasNotCompletedMission =
	            missionSessionRepository.existsByLearningSession_SessionIdAndMissionStatusNot(
	                    sessionId,
	                    MissionSessionStatus.COMPLETED
	            );
	    
	    String evaluation = null;

	    if (!hasNotCompletedMission) {
	    	
	        learningSession.complete();
	        
	        List<SessionMessage> messageHistory = sessionMessageRepository.findBySession_SessionIdOrderByCreatedAtAsc(sessionId);

	        Scenario scenario = learningSession
	                .getPlace()
	                .getScenario();

	        FastApiEvaluationDto.RequestEvaluation fastApiRequest = FastApiEvaluationDto.RequestEvaluation.builder()
	                        .scenarioPrompt(scenario.getPrompt())
	                        .scenarioCategory(scenario.getCategory())
	                        .messages(messageHistory.stream()
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

	    return UserMissionCompleteDto.ResponseComplete.of(missionSession, learningSession, evaluation);
	}
	
	

}
