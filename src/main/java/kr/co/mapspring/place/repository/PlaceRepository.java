package kr.co.mapspring.place.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long>{

	boolean existsByGooglePlaceId(String googlePlaceId);
	
	List<Place> findByPlaceNameContaining(String keyword);
}
