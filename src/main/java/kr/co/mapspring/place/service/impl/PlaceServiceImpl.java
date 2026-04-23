package kr.co.mapspring.place.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.PlaceAlreadyExistsException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.ReadPlaceDto;
import kr.co.mapspring.place.dto.SavePlaceDto;
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

	// 장소 저장
	@Override
	@Transactional
	public void savePlace(SavePlaceDto.RequestSave request) {
		
		Long regionId = request.getRegionId();
		Long scenarioId = request.getScenarioId();
		String googlePlaceId = request.getGooglePlaceId();
		
		boolean placeExists = placeRepository.existsByGooglePlaceId(googlePlaceId);
		
		if (placeExists) {
			throw new PlaceAlreadyExistsException();
		}
								
		Region regionEntity = regionRepository.findById(regionId)
				.orElseThrow(RegionNotFoundException::new);
		
		Scenario scenarioEntity = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		Place place = Place.of(request.getGooglePlaceId(),
							   request.getPlaceName(),
							   request.getPlaceAddress(),
							   request.getPlaceDescription(),
							   request.getLatitude(),
							   request.getLongitude(),
							   scenarioEntity,
							   regionEntity
							   );

		placeRepository.save(place);
	}
	
	// 장소 조회
	@Override
	@Transactional(readOnly = true)
	public ReadPlaceDto.ResponseRead readPlace(ReadPlaceDto.RequestRead request) {
		
		Long placeId = request.getPlaceId();
		
		Place place = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		ReadPlaceDto.ResponseRead response = ReadPlaceDto.ResponseRead.from(place);
		
		return response;
	}
}
