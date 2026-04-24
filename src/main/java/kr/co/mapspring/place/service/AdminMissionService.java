package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.AdminCreateMissionDto;

public interface AdminMissionService {
	
	void createMission (AdminCreateMissionDto.RequestCreate request);
}