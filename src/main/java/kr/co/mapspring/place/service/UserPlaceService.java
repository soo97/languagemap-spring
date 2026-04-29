package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.UserReadPlaceDto;

public interface UserPlaceService {
	
	public UserReadPlaceDto.ResponseRead placeDetail(Long placeId);

}
