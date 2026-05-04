package kr.co.mapspring.place.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import kr.co.mapspring.place.entity.SessionMessage;
import kr.co.mapspring.place.enums.SessionMessageRole;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.MissionSessionRepository;
import kr.co.mapspring.place.repository.PlaceRepository;
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
		
		// FastApi 연결 전 임시 데이터
		String aiMessage = "Hello! Let's start this mission.";
		
		SessionMessage aiStartMessage = SessionMessage.create(
		        missionSession.getLearningSession(),
		        aiMessage,
		        SessionMessageRole.ASSISTANT
		);
		
		sessionMessageRepository.save(aiStartMessage);
		
		Mission mission = missionSession.getMission();
		
		UserMissionStartDto.ResponseMissionStart response = UserMissionStartDto.ResponseMissionStart.of(mission, missionSession, aiMessage);
		
		return response;
	}
	
	

}
