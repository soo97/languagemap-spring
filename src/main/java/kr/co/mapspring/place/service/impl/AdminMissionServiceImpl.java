package kr.co.mapspring.place.service.impl;

import kr.co.mapspring.global.exception.place.MissionNotFoundException;
import kr.co.mapspring.place.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.AdminMissionService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

	// 미션 상세 조회
	@Override
	@Transactional(readOnly = true)
	public AdminReadMissionDto.ResponseRead readMission(Long missionId) {

		Mission mission = missionRepository.findById(missionId)
				.orElseThrow(MissionNotFoundException::new);

		return AdminReadMissionDto.ResponseRead.from(mission);
	}

	// 미션 리스트 조회 및 검색
	@Override
	@Transactional(readOnly = true)
	public List<AdminMissionListDto.ResponseList> missionList(String keyword) {

		List<AdminMissionListDto.ResponseList> responseList;

		if(keyword == null || keyword.isBlank()) {
			List<Mission> mission = missionRepository.findAll();

			responseList = mission.stream()
					.map(AdminMissionListDto.ResponseList::from)
					.collect(Collectors.toList());
		} else {

			List<Mission> mission = missionRepository.findByMissionTitleContaining(keyword);

			responseList = mission.stream()
					.map(AdminMissionListDto.ResponseList::from)
					.collect(Collectors.toList());
		}

		return responseList;
	}

	// 미션 수정
	@Override
	@Transactional
	public void updateMission(Long missionId, AdminUpdateMissionDto.RequestUpdate request) {

		Mission mission = missionRepository.findById(missionId)
				.orElseThrow(MissionNotFoundException::new);

		Long scenarioId = request.getScenarioId();;

		Scenario scenario = scenarioRepository.findById(scenarioId)
						.orElseThrow(ScenarioNotFoundException::new);

		mission.update(request.getMissionTitle(),
					   request.getMissionDescription(),
				       scenario);
	}

	// 미션 삭제
	@Override
	@Transactional
	public void deleteMission(Long missionId) {
		Mission mission = missionRepository.findById(missionId)
				.orElseThrow(MissionNotFoundException::new);

		missionRepository.delete(mission);
	}

 
}
