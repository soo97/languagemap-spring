
package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.AdminCreatePlaceDto;
import kr.co.mapspring.place.dto.AdminReadPlaceDto;
import kr.co.mapspring.place.dto.AdminUpdatePlaceDto;

public interface AdminPlaceService {

	 void savePlace(AdminCreatePlaceDto.RequestCreate request);
		
	 AdminReadPlaceDto.ResponseRead readPlace(AdminReadPlaceDto.RequestRead request); 
	 
	 void updatePlace(Long placeId, AdminUpdatePlaceDto.RequestUpdate request);
		
		
}
