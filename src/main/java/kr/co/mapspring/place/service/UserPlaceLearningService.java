package kr.co.mapspring.place.service;

import kr.co.mapspring.place.dto.UserCreateLearningSessionDto;
import kr.co.mapspring.place.dto.UserReadPlaceDto;

public interface UserPlaceLearningService {
	
	UserReadPlaceDto.ResponseRead markerDetail(Long placeId);
	
	UserCreateLearningSessionDto.ResponseCreate learningStart(Long placeId, UserCreateLearningSessionDto.RequestCreate request);

}
