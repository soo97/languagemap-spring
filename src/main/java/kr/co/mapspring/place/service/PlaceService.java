package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.SavePlaceDto;
import kr.co.mapspring.place.dto.SavePlaceDto.ResponseSaveDto;

public interface PlaceService {
	
	void savePlace(SavePlaceDto.RequestSaveDto request);

}
