package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserMissionStartDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;

public interface UserPlaceLearningService {
	
	/**
	 * 사용자 마커 클릭시 상세 정보, 미션 정보 조회
	 * 
	 * @param placeId 조회할 마커 id
	 * @return 마커(장소)의 상세 정보, 마커의 미션 리스트 정보
	 */
	UserReadPlaceDto.ResponseRead markerDetail(Long placeId);
	
	/**
	 * 학습 시작 버튼 클릭시 학습 세션 생성
	 * 
	 * @param placeId 세션 생성시 필요한 마커 id @PathVariable로 사용하기위해 분리
	 * @param request 세션 생성시 필요한 값(시나리오 레벨, 사용자 id)
	 * @return 생성한 세션의 Id
	 */
	UserCreateLearningSessionDto.ResponseCreate learningStart(Long placeId, UserCreateLearningSessionDto.RequestCreate request);

	/**
	 * 미션 시작 버튼 클릭시 미션 상태 RUNNING으로 변경 후 미션에 대한 ai 첫 대화 반환
	 * 
	 * @param sessionId 미션 세션 조회용
	 * @param missionId 미션 세션 조회용
	 * @return 미션 세션 정보 및 사용자에게 보여줄 ai 메세지
	 */
	UserMissionStartDto.ResponseMissionStart missionStart(Long sessionId, Long missionId);
}
