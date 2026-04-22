package kr.co.mapspring.place.serviceImpl;

import org.springframework.stereotype.Service;

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
																	// TODO: 테스트 코드 작성 완료 후 커스텀 Exception으로 교체
		Region regionEntity = regionRepository.findById(request.getRegion()).orElseThrow(() -> new RuntimeException());
		Scenario scenarioEntity = scenarioRepository.findById(request.getScenario()).orElseThrow(() -> new RuntimeException());
		Place placeEntity = placeRepository.findByGooglePlaceId(request.getGooglePlaceId());

		if (placeEntity != null) {
			throw new RuntimeException("이미 존재하는 장소입니다");
		}
		Place place = Place.of(request, regionEntity, scenarioEntity);

		placeRepository.save(place);
	}
}
