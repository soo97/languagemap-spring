package kr.co.mapspring.place.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.place.dto.SavePlaceDto;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.serviceImpl.PlaceServiceImpl;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {
	
	@InjectMocks
	private PlaceServiceImpl placeService;
	
	@Mock
	private PlaceRepository placeRepository;
	
	@Mock
	private RegionRepository regionRepository;
	
	@Mock
	private ScenarioRepository scenarioRepository;

	@Test
	void 장소_저장_성공() {
		// given
		SavePlaceDto.RequestSaveDto request = SavePlaceDto.RequestSaveDto.builder()
				.googlePlaceId("csdf34asd")
				.placeName("스타벅스")
				.placeDescription("커피 파는 곳")
				.latitude(new BigDecimal("111.1"))
				.longitude(new BigDecimal("32.11441"))
				.scenario(1L)
				.region(1L)
				.build();
		
		when(placeRepository.findByGooglePlaceId(request.getGooglePlaceId()))
		.thenReturn(null);
		
		Region region = Region.builder().regionId(1L).build();
		Scenario scenario = Scenario.builder().scenarioId(1L).build();
		
		when(regionRepository.findById(request.getRegion()))
        .thenReturn(Optional.of(region));

		when(scenarioRepository.findById(request.getScenario()))
        .thenReturn(Optional.of(scenario));
		
		Place savePlace = Place.builder()
				.placeId(1L)
	            .googlePlaceId(request.getGooglePlaceId())
	            .placeName(request.getPlaceName())
	            .placeDescription(request.getPlaceDescription())
	            .latitude(request.getLatitude())
	            .longitude(request.getLongitude())
	            .scenario(scenario)
	            .region(region)
	            .build();
		
		when(placeRepository.save(any(Place.class)))
		.thenReturn(savePlace);
		// when
		placeService.savePlace(request);
		// then
		verify(placeRepository, times(1)).save(any(Place.class));
	}

}
