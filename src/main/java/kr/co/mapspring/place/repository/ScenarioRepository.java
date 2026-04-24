package kr.co.mapspring.place.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.Scenario;

public interface ScenarioRepository extends JpaRepository<Scenario, Long>{

	List<Scenario> findByCategoryContaining(String keyword);

}
