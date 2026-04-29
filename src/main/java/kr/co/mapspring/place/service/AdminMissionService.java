package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.AdminCreateMissionDto;
import kr.co.mapspring.place.dto.AdminMissionListDto;
import kr.co.mapspring.place.dto.AdminReadMissionDto;
import kr.co.mapspring.place.dto.AdminUpdateMissionDto;

import java.util.List;

public interface AdminMissionService {
	
	/**
	 * 관리자 미션 생성 기능
	 * 
	 * @param request 미션 생성 시 필요한 값
	 */
	void createMission(AdminCreateMissionDto.RequestCreate request);

	/**
	 * 관리자 미션 상세 조회 기능
	 *
	 * @param missionId 미션 상세 조회 시 필요한 미션 id
	 * @return 미션 상세 값
	 */
	AdminReadMissionDto.ResponseRead readMission(Long missionId);

	/**
	 *
	 * 관리자 미션 리스트 조회 및 검색 기능
	 * @param keyword 검색 키워드
	 * @return 검색 키워드가 빈 문자열이거나 null이면 전체 조회
	 */
	List<AdminMissionListDto.ResponseList> missionList(String keyword);

	/**
	 * 관리자 미션 수정 기능
	 *
	 * @param missionId 수정할 미션 조회용 미션id
	 * @param request 수정할 값
	 */
	void updateMission(Long missionId, AdminUpdateMissionDto.RequestUpdate request);

	/**
	 * 관리자 미션 삭제 기능
	 *
	 * @param missionId 삭제할 미션 조회용 미션id
	 */
	public void deleteMission(Long missionId);

}