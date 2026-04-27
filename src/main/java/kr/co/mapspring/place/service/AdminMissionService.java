package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.AdminCreateMissionDto;

public interface AdminMissionService {
	
	/**
	 * 관리자 미션 생성 기능
	 * 
	 * @param request 미션 생성 시 필요한 값
	 */
	void createMission (AdminCreateMissionDto.RequestCreate request);
}