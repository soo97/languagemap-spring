package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;

public interface AdminScenarioService {
	
	void createScenario(AdminCreateScenarioDto.RequestCreate request); 
		
	AdminReadScenarioDto.ResponseRead readScenario(AdminReadScenarioDto.RequestRead request); 

}