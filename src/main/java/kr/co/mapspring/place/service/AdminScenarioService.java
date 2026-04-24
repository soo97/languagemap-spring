package kr.co.mapspring.place.service;

import java.util.List;

import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;

public interface AdminScenarioService {
	
	void createScenario(AdminCreateScenarioDto.RequestCreate request); 
		
	AdminReadScenarioDto.ResponseRead readScenario(AdminReadScenarioDto.RequestRead request); 

	void updateScenario(Long ScenarioId, AdminUpdateScenarioDto.RequestUpdate request);
	
	void deleteScenario(Long ScenarioId);
	
	List<AdminScenarioListDto.ResponseList> ScenarioList(String keyword);
}