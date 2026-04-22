package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
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
                .scenarioId(1L)
                .regionId(1L)
                .build();

        Region region = Region.builder()
                .regionId(1L)
                .build();

        Scenario scenario = Scenario.builder()
                .scenarioId(1L)
                .build();

        when(placeRepository.existsByGooglePlaceId(request.getGooglePlaceId()))
                .thenReturn(false);

        when(regionRepository.findById(request.getRegionId()))
                .thenReturn(Optional.of(region));

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.of(scenario));

        // when
        placeService.savePlace(request);

        // then
        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    void 장소_저장_실패_중복된_구글장소ID() {
        // given
        SavePlaceDto.RequestSaveDto request = SavePlaceDto.RequestSaveDto.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(1L)
                .regionId(1L)
                .build();

        when(placeRepository.existsByGooglePlaceId(request.getGooglePlaceId()))
                .thenReturn(true);

        // when & then
        assertThrows(RuntimeException.class, () -> placeService.savePlace(request));
        verify(placeRepository, never()).save(any(Place.class));
    }

    @Test
    void 장소_저장_실패_존재하지_않는_지역() {
        // given
        SavePlaceDto.RequestSaveDto request = SavePlaceDto.RequestSaveDto.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(1L)
                .regionId(999L)
                .build();

        when(placeRepository.existsByGooglePlaceId(request.getGooglePlaceId()))
                .thenReturn(false);

        when(regionRepository.findById(request.getRegionId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(RegionNotFoundException.class, () -> placeService.savePlace(request));
        verify(placeRepository, never()).save(any(Place.class));
    }
    
    @Test
    void 장소_저장_실패_존재하지_않는_시나리오() {
        // given
        SavePlaceDto.RequestSaveDto request = SavePlaceDto.RequestSaveDto.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(999L)
                .regionId(1L)
                .build();

        Region region = Region.builder()
                .regionId(1L)
                .build();

        when(placeRepository.existsByGooglePlaceId(request.getGooglePlaceId()))
                .thenReturn(false);

        when(regionRepository.findById(request.getRegionId()))
                .thenReturn(Optional.of(region));

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class, () -> placeService.savePlace(request));
        verify(placeRepository, never()).save(any(Place.class));
    }
}