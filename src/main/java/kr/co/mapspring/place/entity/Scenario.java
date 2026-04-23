package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import kr.co.mapspring.place.enums.ScenarioLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scenario")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Scenario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scenario_id", nullable = false, updatable = false)
	private Long scenarioId;
	
	@Lob
	@Column(name = "prompt", nullable = false, columnDefinition = "TEXT")
	private String prompt;
	
	@Column(name = "scenarios_description", nullable = false, length = 100)
	private String scenariosDescription;
	
	@Column(name = "complete_exp", nullable = false)
	private Integer completeExp;
	
	@Column(name = "level", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private ScenarioLevel level;
	
	@Column(name = "category", nullable = false, length = 50)
	private String category;
	
	// 테스트 코드 실행용
	public static Scenario from(Long scenarioId) {
		Scenario scenario = new Scenario();
	
		scenario.scenarioId = scenarioId;
		
		return scenario;
	}

}
