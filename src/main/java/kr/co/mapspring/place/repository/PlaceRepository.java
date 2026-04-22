package kr.co.mapspring.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long>{

	Place existsByGooglePlaceId(String googlePlaceId);
}
