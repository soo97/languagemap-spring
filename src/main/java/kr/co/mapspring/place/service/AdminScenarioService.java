package kr.co.mapspring.place.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.ScenarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminScenarioService {
	
	private final ScenarioRepository scenarioRepository;
	
	// 시나리오 생성
	@Transactional
	public void createScenario(AdminCreateScenarioDto.RequestCreate request) {
		
		Scenario scenario = Scenario.create(request.getPrompt(),
											request.getScenarioDescription(),
											request.getCompleteExp(),
											request.getLevel(),
											request.getCategory());	
		
		scenarioRepository.save(scenario);
	}
	
	// 시나리오 조회
	@Transactional(readOnly = true)
	public AdminReadScenarioDto.ResponseRead readScenario(AdminReadScenarioDto.RequestRead request) {
		
		Long scenarioId = request.getScenarioId();
		
		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		return AdminReadScenarioDto.ResponseRead.from(scenario);
		
	}
	
	

}
