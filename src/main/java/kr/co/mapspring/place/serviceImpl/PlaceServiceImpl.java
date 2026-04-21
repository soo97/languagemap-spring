package kr.co.mapspring.place.serviceImpl;

import org.springframework.stereotype.Service;

import kr.co.mapspring.place.dto.SavePlaceDto;
import kr.co.mapspring.place.dto.SavePlaceDto.ResponseSaveDto;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.PlaceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
	
	private final PlaceRepository placeRepository;
	private final RegionRepository regionRepository;
	private final ScenarioRepository scenarioRepository;
	
	public ResponseSaveDto savePlace(SavePlaceDto.RequsetSaveDto request) {
																					// TODO: 테스트 코드 작성 완료 후 커스텀 Exception으로 교체
		Region regionEntity = regionRepository.findById(request.getRegion()).orElseThrow(() -> new RuntimeException());
		Scenario scenarioEntity = scenarioRepository.findById(request.getScenario()).orElseThrow(() -> new RuntimeException());
		Place placeEntity = placeRepository.findByGooglePlaceId(request.getGooglePlaceId());
		
		if (placeEntity == null) {
		Place place = Place.builder()
				.googlePlaceId(request.getGooglePlaceId())
				.placeName(request.getPlaceName())
				.placeDescription(request.getPlaceDescription())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.scenario(scenarioEntity)
				.region(regionEntity)
				.build();
		
		Place savePlace = placeRepository.save(place);
		
		SavePlaceDto.ResponseSaveDto response = SavePlaceDto.ResponseSaveDto.builder()
				.googlePlaceId(savePlace.getGooglePlaceId())
				.placeName(savePlace.getPlaceName())
				.placeDescription(savePlace.getPlaceDescription())
				.latitude(savePlace.getLatitude())
				.longitude(savePlace.getLongitude())
				.scenario(savePlace.getScenario())
				.region(savePlace.getRegion())
				.build();
		
		return response;
		
		} else {
			throw new RuntimeException("이미 존재하는 장소입니다.");
		}
		
		
		
		
	}

}
