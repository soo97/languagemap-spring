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

import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateRegionDto;
import kr.co.mapspring.place.dto.AdminRegionDto;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.service.impl.AdminRegionServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminRegionServiceTest {

    @InjectMocks
    private AdminRegionServiceImpl adminRegionService;

    @Mock
    private RegionRepository regionRepository;

    // ===== 생성 =====
    @Test
    @DisplayName("지역 생성 성공")
    void 지역_생성_성공() {
        AdminCreateRegionDto.RequestCreate request = new AdminCreateRegionDto.RequestCreate();
//        request.setCountry("Korea");
//        request.setCity("Seoul");
//        request.setLatitude(new BigDecimal("37.12345678"));
//        request.setLongitude(new BigDecimal("127.12345678"));

        adminRegionService.createRegion(request);

        verify(regionRepository, times(1)).save(any(Region.class));
    }

    // ===== 조회 =====
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

        AdminRegionDto.ResponseRead result =
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

        List<AdminRegionDto.ResponseList> result =
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

        List<AdminRegionDto.ResponseList> result =
                adminRegionService.regionList("Seoul");

        assertEquals(1, result.size());

        verify(regionRepository).findByCityContaining("Seoul");
        verify(regionRepository, never()).findAll();
    }

    // ===== 수정 =====
    @Test
    @DisplayName("지역 수정 성공")
    void 지역_수정() {
        Region region = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));

        AdminRegionDto.RequestUpdate request = new AdminRegionDto.RequestUpdate();
        request.setCountry("USA");
        request.setCity("NewYork");
        request.setLatitude(new BigDecimal("10"));
        request.setLongitude(new BigDecimal("20"));

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

        AdminRegionDto.RequestUpdate request = new AdminRegionDto.RequestUpdate();

        assertThrows(RegionNotFoundException.class,
                () -> adminRegionService.updateRegion(1L, request));
    }

    // ===== 삭제 =====
    @Test
    @DisplayName("지역 삭제 성공")
    void 지역_삭제() {
        Region region = Region.create("Korea", "Seoul",
                new BigDecimal("1"), new BigDecimal("1"));

        when(regionRepository.findById(1L))
                .thenReturn(Optional.of(region));

        adminRegionService.deleteRegion(1L);

        verify(regionRepository).delete(region);
    }

    @Test
    @DisplayName("지역 삭제 실패")
    void 지역_삭제_실패() {
        when(regionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RegionNotFoundException.class,
                () -> adminRegionService.deleteRegion(1L));

        verify(regionRepository, never()).delete(any());
    }
}
