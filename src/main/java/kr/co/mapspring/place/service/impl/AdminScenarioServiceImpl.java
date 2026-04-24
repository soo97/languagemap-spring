package kr.co.mapspring.place.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.AdminScenarioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminScenarioServiceImpl implements AdminScenarioService{
	
	private final ScenarioRepository scenarioRepository;
	
	// 시나리오 생성
	@Override
	@Transactional
	public void createScenario(AdminCreateScenarioDto.RequestCreate request) {
		
		Scenario scenario = Scenario.create(request.getPrompt(),
											request.getScenarioDescription(),
											request.getCompleteExp(),
											request.getLevel(),
											request.getCategory());	
		
		scenarioRepository.save(scenario);
	}
	
	// 시나리오 상세 조회
	@Override
	@Transactional(readOnly = true)
	public AdminReadScenarioDto.ResponseRead readScenario(AdminReadScenarioDto.RequestRead request) {
		
		Long scenarioId = request.getScenarioId();
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		return AdminReadScenarioDto.ResponseRead.from(scenario);	
	}
	
	// 시나리오 리스트 조회 및 검색
	@Override
	@Transactional(readOnly = true)
	public List<AdminScenarioListDto.ResponseList> scenarioList(String keyword) {
		
		List<AdminScenarioListDto.ResponseList> responseList;
		
		if(keyword == null || keyword.isBlank()) {
			List<Scenario> scenario = scenarioRepository.findAll();
			
			responseList = scenario.stream()
					.map(AdminScenarioListDto.ResponseList::from)
					.collect(Collectors.toList());
		} else {
			List<Scenario> scenario = scenarioRepository.findByCategoryContaining(keyword);
			
			responseList = scenario.stream()
					.map(AdminScenarioListDto.ResponseList::from)
					.collect(Collectors.toList());
			}
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
					request.getLevel(),
					request.getCategory(),
					request.getCompleteExp());
	}
	
	
	// 시나리오 삭제
	@Override
	@Transactional
	public void deleteScenario(Long scenarioId) {
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		scenarioRepository.delete(scenario);
	}
	
	

}
