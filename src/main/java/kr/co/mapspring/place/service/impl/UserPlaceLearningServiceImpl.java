package kr.co.mapspring.place.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.user.UserNotFoundException;
import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.repository.LearningSessionRepository;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.PlaceRepository;
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
	
	// 마커 상세 정보 조회
	@Override
	@Transactional(readOnly = true)
	public UserReadPlaceDto.ResponseRead markerDetail(Long placeId) {
		
		Place placeDetail = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		List<Mission> missionList = missionRepository.findByScenarioId(placeDetail.getScenario().getScenarioId());
		
		return UserReadPlaceDto.ResponseRead.from(placeDetail, missionList);
	}
	
	// 학습하기 클릭 시 학습 세션 생성
	@Override
	@Transactional
	public UserCreateLearningSessionDto.ResponseCreate learningStart(Long placeId, UserCreateLearningSessionDto.RequestCreate request) {
		
		Place place = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		Long userId = request.getUserId();
		
		User user = userRepository.findById(userId)
				.orElseThrow(UserNotFoundException::new);
		
		LearningSession learningSession = LearningSession.create(place, user, request.getLevel());
		
		LearningSession response = learningSessionRepository.save(learningSession);
		
		return UserCreateLearningSessionDto.ResponseCreate.from(response);
	}
	
	

}
