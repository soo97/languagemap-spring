package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.SavePlaceDto;

public interface PlaceService {
	
	void savePlace(SavePlaceDto.RequestSave request);
	

}
