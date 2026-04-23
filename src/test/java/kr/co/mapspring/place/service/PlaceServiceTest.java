package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import kr.co.mapspring.place.service.impl.PlaceServiceImpl;

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
    @DisplayName("장소 저장 성공")
    void 장소_저장_성공() {
        // given
        SavePlaceDto.RequestSave request = SavePlaceDto.RequestSave.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(1L)
                .regionId(1L)
                .build();

        Region region = Region.from(1L);

        Scenario scenario = Scenario.from(1L);

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
    @DisplayName("장소 저장 실패 중복된 구글 장소ID")
    void 장소_저장_실패_중복된_구글_장소ID() {
        // given
        SavePlaceDto.RequestSave request = SavePlaceDto.RequestSave.builder()
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
    @DisplayName("장소 저장 실패 존재하지 않는 지역")
    void 장소_저장_실패_존재하지_않는_지역() {
        // given
        SavePlaceDto.RequestSave request = SavePlaceDto.RequestSave.builder()
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
    @DisplayName("장소 저장 실패 존재하지 않는 시나리오")
    void 장소_저장_실패_존재하지_않는_시나리오() {
        // given
        SavePlaceDto.RequestSave request = SavePlaceDto.RequestSave.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(999L)
                .regionId(1L)
                .build();

        Region region = Region.from(1L);

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
    
    @Test
    void 장소_조회_성공() {
        // given
        ReadPlaceDto.RequestRead request = ReadPlaceDto.RequestRead.builder()
        		.placeId(1L)
        		.build();

        Region region = Region.from(10L);

        Scenario scenario = Scenario.from(20L);

        Place place = Place.testOf(1L, 
        					   "google-place-123", "스타벅스 강남점", 
        					   "커피를 주문할 수 있는 장소", 
        					   new BigDecimal("37.12345678"), 
        					   new BigDecimal("127.12345678"), 
        					   region, 
        					   scenario);

        when(placeRepository.findById(request.getPlaceId()))
                .thenReturn(Optional.of(place));

        // when
        ReadPlaceDto.ResponseRead response = placeService.readPlace(request);

        // then
        assertEquals("스타벅스 강남점", response.getPlaceName());
        assertEquals("커피를 주문할 수 있는 장소", response.getPlaceDescription());
        assertEquals(new BigDecimal("37.12345678"), response.getLatitude());
        assertEquals(new BigDecimal("127.12345678"), response.getLongitude());
        assertEquals(scenario.getScenariosDescription(), response.getScenarioDescription());
        assertEquals(region.getCity(), response.getCity());
    }

    @Test
    void 장소_조회_실패_존재하지_않는_장소() {
        // given
        ReadPlaceDto.RequestRead request = ReadPlaceDto.RequestRead.builder()
        		.placeId(99L)
        		.build();

        when(placeRepository.findById(request.getPlaceId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(PlaceNotFoundException.class, () -> placeService.readPlace(request));
    }
}
