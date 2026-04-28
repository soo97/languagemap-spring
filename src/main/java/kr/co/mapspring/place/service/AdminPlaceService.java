
package kr.co.mapspring.place.service;

import java.util.List;

import kr.co.mapspring.place.dto.AdminCreatePlaceDto;
import kr.co.mapspring.place.dto.AdminPlaceListDto;
import kr.co.mapspring.place.dto.AdminPlaceListDto.ResponseList;
import kr.co.mapspring.place.dto.AdminReadPlaceDto;
import kr.co.mapspring.place.dto.AdminUpdatePlaceDto;

public interface AdminPlaceService {


	/**
	 * 관리자 장소 생성 기능
	 * 
	 * @param request 장소 생성 시 필요한 값
	 */
	void savePlace(AdminCreatePlaceDto.RequestCreate request);
		
	/**
	 * 관리자 장소 상세 조회 기능
	 * 장소 수정시 보여줄 값
	 * 
	 * @param request 조회하기위한 장소 Id;
	 * @return 조회한 장소의 세부 데이터
	 */
	AdminReadPlaceDto.ResponseRead readPlace(Long placeId); 
	 
	/**
	 * 관리자 장소 수정 기능
	 * @param placeId 수정할 데이터를 불러오기 위한 Id
	 * @param request 수정할 값
	 */
	void updatePlace(Long placeId, AdminUpdatePlaceDto.RequestUpdate request);
	
	/**
	 * 관리자 장소 삭제 기능
	 * 
	 * @param PlaceId 삭제 장소 조회 Id
	 */
	void deletePlace(Long PlaceId);

	/**
	 * 관리자 장소 리스트 조회 기능
	 * 
	 * @param keyword 검색어(장소 이름)
	 * @return 변수가 포함되는 장소
	 */
	List<AdminPlaceListDto.ResponseList> placeList(String keyword);
}
