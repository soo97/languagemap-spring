package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.global.exception.place.RegionInUseException;
import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateRegionDto;
import kr.co.mapspring.place.dto.AdminReadRegionDto;
import kr.co.mapspring.place.dto.AdminRegionListDto;
import kr.co.mapspring.place.dto.AdminUpdateRegionDto;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.service.impl.AdminRegionServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminRegionServiceTest {

    @InjectMocks
    private AdminRegionServiceImpl adminRegionService;

    @Mock
    private RegionRepository regionRepository;
    
    @Mock
    private PlaceRepository placeRepository;

    @Test
    @DisplayName("지역 생성 성공")
    void 지역_생성_성공() {
    	AdminCreateRegionDto.RequestCreate request = AdminCreateRegionDto.RequestCreate.builder()
    			.country("korea")
    			.city("Seoul")
    			.latitude(new BigDecimal("37.12345678"))
    			.longitude(new BigDecimal("127.12345678"))
    			.build();

        adminRegionService.createRegion(request);

        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    @DisplayName("지역 상세 조회 성공")
    void 지역_조회_성공() {
        Region region = Region.create(
                "Korea",
                "Seoul",
                new BigDecimal("37.12345678"),
                new BigDecimal("127.12345678")
        );

        when(regionRepository.findById(1L))
                .thenReturn(Optional.of(region));

        AdminReadRegionDto.ResponseRead result =
                adminRegionService.readRegion(1L);

        assertEquals("Korea", result.getCountry());
        assertEquals("Seoul", result.getCity());
    }

    @Test
    @DisplayName("지역 상세 조회 실패")
    void 지역_조회_실패() {
        when(regionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RegionNotFoundException.class,
                () -> adminRegionService.readRegion(1L));
    }

    // ===== 리스트 =====
    @Test
    @DisplayName("지역 리스트 조회 성공")
    void 지역_리스트() {
        Region r1 = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));
        Region r2 = Region.create("Japan", "Tokyo",
                new BigDecimal("2"), new BigDecimal("2"));

        when(regionRepository.findAll())
                .thenReturn(List.of(r1, r2));

        List<AdminRegionListDto.ResponseList> result =
                adminRegionService.regionList(null);

        assertEquals(2, result.size());

        verify(regionRepository).findAll();
        verify(regionRepository, never()).findByCityContaining(any());
    }

    @Test
    @DisplayName("지역 검색 성공")
    void 지역_검색() {
        Region region = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));

        when(regionRepository.findByCityContaining("Seoul"))
                .thenReturn(List.of(region));

        List<AdminRegionListDto.ResponseList> result =
                adminRegionService.regionList("Seoul");

        assertEquals(1, result.size());

        verify(regionRepository).findByCityContaining("Seoul");
        verify(regionRepository, never()).findAll();
    }

    @Test
    @DisplayName("지역 수정 성공")
    void 지역_수정() {
        Region region = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));
        
        AdminUpdateRegionDto.RequestUpdate request = AdminUpdateRegionDto.RequestUpdate.builder()
    			.country("USA")
    			.city("NewYork")
    			.latitude(new BigDecimal("10"))
    			.longitude(new BigDecimal("20"))
    			.build();

        when(regionRepository.findById(1L))
                .thenReturn(Optional.of(region));

        adminRegionService.updateRegion(1L, request);

        assertEquals("USA", region.getCountry());
        assertEquals("NewYork", region.getCity());
    }

    @Test
    @DisplayName("지역 수정 실패")
    void 지역_수정_실패() {
        when(regionRepository.findById(1L))
                .thenReturn(Optional.empty());

        AdminUpdateRegionDto.RequestUpdate request = new AdminUpdateRegionDto.RequestUpdate();

        assertThrows(RegionNotFoundException.class,
                () -> adminRegionService.updateRegion(1L, request));
    }

    @Test
    @DisplayName("지역 삭제 성공 참조하는 장소 없음")
    void 지역_삭제_성공() {
        Region region = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));

        when(regionRepository.findById(1L))
                .thenReturn(Optional.of(region));

        when(placeRepository.existsByRegion_RegionId(1L))
                .thenReturn(false);

        adminRegionService.deleteRegion(1L);

        verify(regionRepository).delete(region);
    }
    
    @Test
    @DisplayName("지역 삭제 실패 지역 없음")
    void 지역_삭제_실패_지역없음() {
        when(regionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RegionNotFoundException.class,
                () -> adminRegionService.deleteRegion(1L));

        verify(regionRepository, never()).delete(any());
    }
    
    @Test
    @DisplayName("지역 삭제 실패 참조하는 장소 존재")
    void 지역_삭제_실패_장소존재() {
        Region region = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));

        when(regionRepository.findById(1L))
                .thenReturn(Optional.of(region));

        when(placeRepository.existsByRegion_RegionId(1L))
                .thenReturn(true);

        assertThrows(RegionInUseException.class,
                () -> adminRegionService.deleteRegion(1L));

        verify(regionRepository, never()).delete(any());
    }
}
