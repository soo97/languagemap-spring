package kr.co.mapspring.place.serviceImpl;

import org.springframework.stereotype.Service;

import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
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

	@Override
	public void savePlace(SavePlaceDto.RequestSaveDto request) {
		
		Long regionId = request.getRegionId();
		Long scenarioId = request.getScenarioId();
		String googlePlaceId = request.getGooglePlaceId();
								
		Region regionEntity = regionRepository.findById(regionId)
				.orElseThrow(() -> new RegionNotFoundException());
		
		Scenario scenarioEntity = scenarioRepository.findById(scenarioId)
				.orElseThrow(() -> new ScenarioNotFoundException());
		
		Place placeEntity = placeRepository.existsByGooglePlaceId(googlePlaceId);

		if (placeEntity != null) {
			throw new RuntimeException("이미 존재하는 장소입니다");
		}
		Place place = Place.of(request.getGooglePlaceId(),
							   request.getPlaceName(),
							   request.getPlaceDescription(),
							   request.getLatitude(),
							   request.getLongitude(),
							   scenarioEntity,
							   regionEntity
							   );

		placeRepository.save(place);
	}
}
