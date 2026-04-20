package kr.co.mapspring.place.serviceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.mapspring.place.dto.SavePlaceDto;
import kr.co.mapspring.place.entity.Place;
import kr.co.mapspring.place.repository.PlaceRepository;
import kr.co.mapspring.place.service.PlaceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{
	
	private final PlaceRepository placeRepository;
	
	@Override
	@Transactional
	public SavePlaceDto.ResponseSaveDto savePlace(SavePlaceDto.RequsetSaveDto request) {
		      Place place = placeRepository.findByGooglePlaceId(request.getGooglePlaceId());
		      
		      if(place == null) {
		    	  Place savePlace = Place.placeFrom(request);
		    	  placeRepository.save(savePlace);
		    	  return SavePlaceDto.SavePlaceDtoFrom(savePlace);
		      } else {
		    	  return SavePlaceDto.SavePlaceDtoFrom(place);
		      }
	}

}
