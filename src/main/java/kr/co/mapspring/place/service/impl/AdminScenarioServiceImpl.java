package kr.co.mapspring.place.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.ScenarioInUseException;
import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.AdminScenarioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminScenarioServiceImpl implements AdminScenarioService{
	
	private final ScenarioRepository scenarioRepository;
	private final MissionRepository missionRepository;
	private final PlaceRepository placeRepository;
	
	// 시나리오 생성
	@Override
	@Transactional
	public void createScenario(AdminCreateScenarioDto.RequestCreate request) {
		
		Scenario scenario = Scenario.create(request.getPrompt(),
											request.getScenarioDescription(),
											request.getCompleteExp(),
											request.getCategory());	
		
		scenarioRepository.save(scenario);
	} 
	
	// 시나리오 상세 조회
	@Override
	@Transactional(readOnly = true)
	public AdminReadScenarioDto.ResponseRead readScenario(Long scenarioId) {
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		return AdminReadScenarioDto.ResponseRead.from(scenario);	
	}
	
	// 시나리오 리스트 조회 및 검색
	@Override
	@Transactional(readOnly = true)
	public List<AdminScenarioListDto.ResponseList> scenarioList(String keyword) {
	
		List<Scenario> scenarioList;
		
		String normalizedKeyword = (keyword == null) ? null : keyword.trim();
		
		if(normalizedKeyword == null || normalizedKeyword.isBlank()) {
			
			scenarioList = scenarioRepository.findAll();
			
		} else {
			
			scenarioList = scenarioRepository.findByCategoryContaining(normalizedKeyword);
			
			}
		
		List<AdminScenarioListDto.ResponseList> responseList = scenarioList.stream()
				.map(AdminScenarioListDto.ResponseList::from)
				.toList();
		
		return responseList;
	}
	
	// 시나리오 수정
	@Override
	@Transactional
	public void updateScenario(Long scenarioId, AdminUpdateScenarioDto.RequestUpdate request) {
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		scenario.update(request.getPrompt(),
					request.getScenarioDescription(),
					request.getCategory(),
					request.getCompleteExp());
	}
	
	
	// 시나리오 삭제
	@Override
	@Transactional
	public void deleteScenario(Long scenarioId) {
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		if (missionRepository.existsByScenario_ScenarioId(scenarioId)) {
	        throw new ScenarioInUseException();
	    }

	    if (placeRepository.existsByScenario_ScenarioId(scenarioId)) {
	        throw new ScenarioInUseException();
	    }
		
		scenarioRepository.delete(scenario);
	}
	
	

}
