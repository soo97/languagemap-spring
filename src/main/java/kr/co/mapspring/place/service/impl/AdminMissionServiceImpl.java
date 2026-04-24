package kr.co.mapspring.place.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateMissionDto;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.AdminMissionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMissionServiceImpl implements AdminMissionService{
	
	private final ScenarioRepository scenarioRepository;
	private final MissionRepository missionRepository;
	
	// 미션 생성
	@Override
	@Transactional
	public void createMission(AdminCreateMissionDto.RequestCreate request) {
		
		Long scenarioId = request.getScenarioId();
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		Mission mission = Mission.create(request.getMissionTitle(),
										 request.getMissionDescription(),
										 scenario);
		
		missionRepository.save(mission);
		
	}
    
}
