package kr.co.mapspring.place.service.impl;

import org.springframework.stereotype.Service;

import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.place.dto.UserReadPlaceDto;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.service.UserPlaceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPlaceServiceImpl implements UserPlaceService {
	
	private final PlaceRepository placeRepository;
	
	// 마커 상세 정보 조회
	public UserReadPlaceDto.ResponseRead placeDetail(Long placeId) {
		
		Place placeDetail = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		return UserReadPlaceDto.ResponseRead.from(placeDetail);
		
		
	}

}
