package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.SavePlaceDto;
import kr.co.mapspring.place.dto.SavePlaceDto.ResponseSaveDto;

public interface PlaceService {
	
	ResponseSaveDto savePlace(SavePlaceDto.RequsetSaveDto request);

}
