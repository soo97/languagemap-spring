package kr.co.mapspring.place.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.global.exception.place.PlaceAlreadyExistsException;
import kr.co.mapspring.global.exception.place.PlaceNotFoundException;
import kr.co.mapspring.global.exception.place.RegionNotFoundException;
import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreatePlaceDto;
import kr.co.mapspring.place.dto.AdminMissionListDto;
import kr.co.mapspring.place.dto.AdminPlaceListDto;
import kr.co.mapspring.place.dto.AdminReadPlaceDto;
import kr.co.mapspring.place.dto.AdminUpdatePlaceDto;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.entity.Region;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.repository.RegionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.AdminPlaceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminPlaceServiceImpl implements AdminPlaceService{

	private final PlaceRepository placeRepository;
	private final RegionRepository regionRepository;
	private final ScenarioRepository scenarioRepository;

	// 장소 생성
	@Override
	@Transactional
	public void savePlace(AdminCreatePlaceDto.RequestCreate request) {
		
		Long regionId = request.getRegionId();
		Long scenarioId = request.getScenarioId();
		String googlePlaceId = request.getGooglePlaceId();
		
		boolean placeExists = placeRepository.existsByGooglePlaceId(googlePlaceId);
		
		if (placeExists) {
			throw new PlaceAlreadyExistsException();
		}
								
		Region regionEntity = regionRepository.findById(regionId)
				.orElseThrow(RegionNotFoundException::new);
		
		Scenario scenarioEntity = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		Place place = Place.create(request.getGooglePlaceId(),
							   request.getPlaceName(),
							   request.getPlaceAddress(),
							   request.getPlaceDescription(),
							   request.getLatitude(),
							   request.getLongitude(),
							   scenarioEntity,
							   regionEntity
							   );

		placeRepository.save(place);
	}
	
	// 장소 상세 조회
	@Override
	@Transactional(readOnly = true)
	public AdminReadPlaceDto.ResponseRead readPlace(Long placeId) {
		
		Place place = placeRepository.findById(placeId) 
				.orElseThrow(PlaceNotFoundException::new);
		
		AdminReadPlaceDto.ResponseRead response = AdminReadPlaceDto.ResponseRead.from(place);
		
		return response;
	}
	
	// 장소 리스트 조회
	@Override
	@Transactional(readOnly = true)
	public List<AdminPlaceListDto.ResponseList> placeList(String keyword) {
		
		List<AdminPlaceListDto.ResponseList> responseList;
		
		String normalizedKeyword = (keyword == null) ? null : keyword.trim();

		if(normalizedKeyword == null || normalizedKeyword.isBlank()) {
			List<Place> place = placeRepository.findAll();

			responseList = place.stream()
					.map(AdminPlaceListDto.ResponseList::from)
					.collect(Collectors.toList());
		} else {

			List<Place> place = placeRepository.findByPlaceNameContaining(normalizedKeyword);

			responseList = place.stream()
					.map(AdminPlaceListDto.ResponseList::from)
					.collect(Collectors.toList());
		}

		return responseList;
	}
	
	// 장소 수정
	@Override
	@Transactional
	public void updatePlace(Long placeId, AdminUpdatePlaceDto.RequestUpdate request) {
			
		Place place = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);

		Long scenarioId = request.getScenarioId();

		Scenario scenario = scenarioRepository.findById(scenarioId)
				.orElseThrow(ScenarioNotFoundException::new);
		
		place.update(request.getPlaceName(),
					 request.getPlaceDescription(),
					 scenario); 
	}
	
	// 장소 삭제
	@Override
	@Transactional
	public void deletePlace(Long placeId) {
		
		Place place = placeRepository.findById(placeId)
				.orElseThrow(PlaceNotFoundException::new);
		
		placeRepository.delete(place);
		
	}

}