package kr.co.mapspring.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.mapspring.place.entity.Mission;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long>{

	List<Mission> findByMissionTitleContaining(String keyword);
	
	boolean existsByScenario_ScenarioId(Long scenarioId);

}
