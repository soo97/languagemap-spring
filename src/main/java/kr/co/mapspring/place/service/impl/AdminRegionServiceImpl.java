package kr.co.mapspring.place.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.RegionInUseException;
import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateRegionDto;
import kr.co.mapspring.place.dto.AdminReadRegionDto;
import kr.co.mapspring.place.dto.AdminRegionListDto;
import kr.co.mapspring.place.dto.AdminUpdateRegionDto;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.service.AdminRegionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminRegionServiceImpl implements AdminRegionService {
	
	 private final RegionRepository regionRepository;
	 private final PlaceRepository placeRepository;

	    // 지역 생성
	    @Override
	    @Transactional
	    public void createRegion(AdminCreateRegionDto.RequestCreate request) {
	        Region region = Region.create(
	                				      request.getCountry(),
	                				      request.getCity(),
	                				      request.getLatitude(),
	                				      request.getLongitude()
	        );

	        regionRepository.save(region);
	    }

	    // 지역 상세 조회
	    @Override
	    @Transactional(readOnly = true)
	    public AdminReadRegionDto.ResponseRead readRegion(Long regionId) {
	        Region region = regionRepository.findById(regionId)
	                .orElseThrow(RegionNotFoundException::new);

	        return AdminReadRegionDto.ResponseRead.from(region);
	    }

	    // 지역 리스트 조회 및 검색
	    @Override
	    @Transactional(readOnly = true)
	    public List<AdminRegionListDto.ResponseList> regionList(String keyword) {

	        List<Region> region;
	        
	        String normalizedKeyword = (keyword == null) ? null : keyword.trim();

	        if (normalizedKeyword == null || normalizedKeyword.isBlank()) {
	            region = regionRepository.findAll();
	        } else {
	            region = regionRepository.findByCityContaining(normalizedKeyword);
	        }

	       
	        List<AdminRegionListDto.ResponseList> regionList = region.stream()
	                .map(AdminRegionListDto.ResponseList::from)
	                .toList();
	        
	        return regionList; 
	    }

	    // 지역 수정
	    @Override
	    @Transactional
	    public void updateRegion(Long regionId, AdminUpdateRegionDto.RequestUpdate request) {
	        Region region = regionRepository.findById(regionId)
	                .orElseThrow(RegionNotFoundException::new);

	        region.update(
	                	  request.getCountry(),
	                	  request.getCity(),
	                	  request.getLatitude(),
	                	  request.getLongitude()
	        );
	    }

	    // 지역 삭제
	    @Override
	    @Transactional
	    public void deleteRegion(Long regionId) {
	        Region region = regionRepository.findById(regionId)
	                .orElseThrow(RegionNotFoundException::new);
	        
	        if (placeRepository.existsByRegion_RegionId(regionId)) {
		        throw new RegionInUseException();
		    }

	        regionRepository.delete(region);
	    }

}
