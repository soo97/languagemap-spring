package kr.co.mapspring.place.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.mapspring.global.exception.place.MissionNotFoundException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.service.UserPlaceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPlaceServiceImpl implements UserPlaceService {
	
	private final PlaceRepository placeRepository;
	private final MissionRepository missionRepository;
	
	// 마커 상세 정보 조회
	public UserReadPlaceDto.ResponseRead placeDetail(Long placeId) {
		
		Place placeDetail = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		Long scenarioId = placeDetail.getScenario().getScenarioId();
		
		Mission missionList = missionRepository.findById(scenarioId)
				.orElseThrow(MissionNotFoundException::new);
		
		return UserReadPlaceDto.ResponseRead.from(placeDetail);
	}
	
	// 마케 조회 시 미션 리스트 조회
	public UserReadMissionDto.ResponseRead MissionList(Long ScenarioId) {
		
		List<Mission> missionList = missionRepository.findByScenarioId(scenarioId);
		
		return UserReadMissionDto.ResponseRead.from(missionList);
	}
	
	

}
