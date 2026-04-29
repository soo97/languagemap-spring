package kr.co.mapspring.place.service;

import java.util.List;

import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;

public interface AdminScenarioService {
	
	/**
	 * 관리자 시나리오 생성 기능
	 * 
	 * @param request 시나리오 생성 시 필요한 입력값.
	 */
	void createScenario(AdminCreateScenarioDto.RequestCreate request); 
	
	
	/**
	 * 관리자 시나리오 상세 조회 기능
	 * 시나리오 수정시 사용
	 * 
	 * @param request 조회할 때 필요한 시나리오 Id
	 * @return 조회한 시나리오 상세 값
	 */
	AdminReadScenarioDto.ResponseRead readScenario(Long scenarioId);
	/**
	 * 관리자 시나리오 수정 기능
	 * 
	 * @param ScenarioId 수정할 시나리오를 찾기 위한 id
	 * @param request 시나리오 수정 값을 담은 변수
	 */
	void updateScenario(Long ScenarioId, AdminUpdateScenarioDto.RequestUpdate request);
	
	/**
	 * 관리자 시나리오 삭제 기능
	 * 
	 * @param ScenarioId 삭제할 데이터를 조회하기 위한 시나리오 id
	 */
	void deleteScenario(Long ScenarioId);
	
	/**
	 * 관리자 시나리오 검색 및 조회 기능
	 * 
	 * @param keyword 검색시 필요한 키워드, 카테고리 종류로 검색 
	 * 		  ex) 카페, 공항 등.... null이나 빈칸이라면 전체 조회
	 * @return 조회된 시나리오 리스트 
	 */
	List<AdminScenarioListDto.ResponseList> scenarioList(String keyword);
}