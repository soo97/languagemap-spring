package kr.co.mapspring.place.service;

import java.util.List;

import kr.co.mapspring.place.dto.AdminCreateRegionDto;
import kr.co.mapspring.place.dto.AdminReadRegionDto;
import kr.co.mapspring.place.dto.AdminRegionListDto;
import kr.co.mapspring.place.dto.AdminUpdateRegionDto;

public interface AdminRegionService {
	
	 void createRegion(AdminCreateRegionDto.RequestCreate request);

	 AdminReadRegionDto.ResponseRead readRegion(Long regionId);

	 List<AdminRegionListDto.ResponseList> regionList(String keyword);

	 void updateRegion(Long regionId, AdminUpdateRegionDto.RequestUpdate request);

	 void deleteRegion(Long regionId);

}
