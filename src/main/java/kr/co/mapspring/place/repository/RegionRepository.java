package kr.co.mapspring.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long>{

}
