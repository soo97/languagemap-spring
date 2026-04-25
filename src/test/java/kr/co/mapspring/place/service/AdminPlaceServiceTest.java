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
import kr.co.mapspring.place.dto.AdminCreatePlaceDto;
import kr.co.mapspring.place.dto.AdminReadPlaceDto;
import kr.co.mapspring.place.dto.AdminUpdatePlaceDto;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.impl.AdminPlaceServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminPlaceServiceTest {

    @InjectMocks
    private AdminPlaceServiceImpl adminPlaceService;
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Test
    @DisplayName("장소 생성 성공")
    void 장소_생성_성공() {
        // given
        AdminCreatePlaceDto.RequestCreate request = AdminCreatePlaceDto.RequestCreate.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(1L)
                .regionId(1L)
                .build();

        Region region = Region.withId(1L);

        Scenario scenario = Scenario.withId(1L);

        when(placeRepository.existsByGooglePlaceId(request.getGooglePlaceId()))
                .thenReturn(false);

        when(regionRepository.findById(request.getRegionId()))
                .thenReturn(Optional.of(region));

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.of(scenario));

        // when
        adminPlaceService.savePlace(request);

        // then
        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    @DisplayName("장소 생성 실패 중복된 구글 장소ID")
    void 장소_생성_실패_중복된_구글_장소ID() {
        // given
        AdminCreatePlaceDto.RequestCreate request = AdminCreatePlaceDto.RequestCreate.builder()
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
        assertThrows(RuntimeException.class, () -> adminPlaceService.savePlace(request));
        verify(placeRepository, never()).save(any(Place.class));
    }

    @Test
    @DisplayName("장소 저장 실패 존재하지 않는 지역")
    void 장소_저장_실패_존재하지_않는_지역() {
        // given
        AdminCreatePlaceDto.RequestCreate request = AdminCreatePlaceDto.RequestCreate.builder()
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
        assertThrows(RegionNotFoundException.class, () -> adminPlaceService.savePlace(request));
        verify(placeRepository, never()).save(any(Place.class));
    }
    
    @Test
    @DisplayName("장소 생성 실패 존재하지 않는 시나리오")
    void 장소_생성_실패_존재하지_않는_시나리오() {
        // given
        AdminCreatePlaceDto.RequestCreate request = AdminCreatePlaceDto.RequestCreate.builder()
                .googlePlaceId("csdf34asd")
                .placeName("스타벅스")
                .placeDescription("커피 파는 곳")
                .latitude(new BigDecimal("111.1"))
                .longitude(new BigDecimal("32.11441"))
                .scenarioId(999L)
                .regionId(1L)
                .build();

        Region region = Region.withId(1L);

        when(placeRepository.existsByGooglePlaceId(request.getGooglePlaceId()))
                .thenReturn(false);

        when(regionRepository.findById(request.getRegionId()))
                .thenReturn(Optional.of(region));

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class, () -> adminPlaceService.savePlace(request));
        verify(placeRepository, never()).save(any(Place.class));
    }
    
    @Test
    @DisplayName("장소 상세 조회 성공")
    void 장소_상세_조회_성공() {
        // given
        AdminReadPlaceDto.RequestRead request = AdminReadPlaceDto.RequestRead.builder()
        		.placeId(1L)
        		.build();

        Region region = Region.withId(10L);

        Scenario scenario = Scenario.withId(20L);

        Place place = Place.testOf(1L, 
        					   "google-place-123",
        					   "스타벅스 강남점",
        					   "인천시 서구",
        					   "커피를 주문할 수 있는 장소", 
        					   new BigDecimal("37.12345678"), 
        					   new BigDecimal("127.12345678"), 
        					   region, 
        					   scenario);

        when(placeRepository.findById(request.getPlaceId()))
                .thenReturn(Optional.of(place));

        // when
        AdminReadPlaceDto.ResponseRead response = adminPlaceService.readPlace(request);

        // then
        assertEquals("스타벅스 강남점", response.getPlaceName());
        assertEquals("커피를 주문할 수 있는 장소", response.getPlaceDescription());
        assertEquals(new BigDecimal("37.12345678"), response.getLatitude());
        assertEquals(new BigDecimal("127.12345678"), response.getLongitude());
        assertEquals(scenario.getScenarioDescription(), response.getScenarioDescription());
        assertEquals(region.getCity(), response.getCity());
    }

    @Test
    @DisplayName("장소 상세 조회 실패 존재하지 않는 장소")
    void 장소_상세_조회_실패_존재하지_않는_장소() {
        // given
        AdminReadPlaceDto.RequestRead request = AdminReadPlaceDto.RequestRead.builder()
        		.placeId(99L)
        		.build();

        when(placeRepository.findById(request.getPlaceId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(PlaceNotFoundException.class, () -> adminPlaceService.readPlace(request));
    }
    
    @Test
    @DisplayName("장소 수정 성공")
    void 장소_수정_성공() {
        // given
        Long placeId = 1L;
        AdminUpdatePlaceDto.RequestUpdate request = AdminUpdatePlaceDto.RequestUpdate.builder()
                .placeName("수정된 장소명")
                .placeDescription("수정된 장소 설명")
                .scenarioId(2L)
                .build();

        Region region = Region.withId(10L);
        Scenario oldScenario = Scenario.withId(1L);
        Scenario newScenario = Scenario.withId(2L);

        Place place = Place.testOf(
                placeId,
                "google-place-123",
                "기존 장소명",
                "인천시 서구",
                "기존 장소 설명",
                new BigDecimal("37.12345678"),
                new BigDecimal("127.12345678"),
                region,
                oldScenario
        );

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.of(place));

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.of(newScenario));

        // when
        adminPlaceService.updatePlace(placeId, request);

        // then
        assertEquals("수정된 장소명", place.getPlaceName());
        assertEquals("수정된 장소 설명", place.getPlaceDescription());
        assertEquals(newScenario, place.getScenario());
    }


    @Test 
    @DisplayName("장소 수정 실패 존재하지 않는 장소")
    void 장소_수정_실패_존재하지_않는_장소() {
        // given
        Long placeId = 999L;

        AdminUpdatePlaceDto.RequestUpdate request = AdminUpdatePlaceDto.RequestUpdate.builder()
                .placeName("수정된 장소명")
                .placeDescription("수정된 장소 설명")
                .scenarioId(2L)
                .build();

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(PlaceNotFoundException.class, () -> adminPlaceService.updatePlace(placeId, request));
        verify(scenarioRepository, never()).findById(any());
    }

    @Test
    @DisplayName("장소 수정 실패 존재하지 않는 시나리오")
    void 장소_수정_실패_존재하지_않는_시나리오() {
        // given
        Long placeId = 1L;

        AdminUpdatePlaceDto.RequestUpdate request = AdminUpdatePlaceDto.RequestUpdate.builder()
                .placeName("수정된 장소명")
                .placeDescription("수정된 장소 설명")
                .scenarioId(999L)
                .build();

        Region region = Region.withId(10L);
        Scenario oldScenario = Scenario.withId(1L);

        Place place = Place.testOf(
                placeId,
                "google-place-123",
                "기존 장소명",
                "인천시 서구",
                "기존 장소 설명",
                new BigDecimal("37.12345678"),
                new BigDecimal("127.12345678"),
                region,
                oldScenario
        );

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.of(place));

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class, () -> adminPlaceService.updatePlace(placeId, request));
    }
    
    @Test
    @DisplayName("장소 삭제 성공")
    void 장소_삭제_성공() {
        // given
        Long placeId = 1L;

        Region region = Region.withId(1L);
        Scenario scenario = Scenario.withId(1L);

        Place place = Place.testOf(
                placeId,
                "google-place-123",
                "스타벅스 강남점",
                "인천시 서구",
                "커피를 주문할 수 있는 장소",
                new BigDecimal("37.12345678"),
                new BigDecimal("127.12345678"),
                region,
                scenario
        );

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.of(place));

        // when
        adminPlaceService.deletePlace(placeId);

        // then
        verify(placeRepository, times(1)).delete(place);
    }
    
    @Test
    @DisplayName("장소 삭제 실패 존재하지 않는 장소")
    void 장소_삭제_실패_존재하지_않는_장소() {
        // given
        Long placeId = 999L;

        when(placeRepository.findById(placeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(PlaceNotFoundException.class,
                () -> adminPlaceService.deletePlace(placeId));

        verify(placeRepository, never()).delete(any());
    }
}
