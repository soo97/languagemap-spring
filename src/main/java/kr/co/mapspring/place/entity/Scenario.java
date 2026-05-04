package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scenario")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Scenario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scenario_id", nullable = false, updatable = false)
	private Long scenarioId;

	@Column(name = "prompt", nullable = false, columnDefinition = "TEXT")
	private String prompt;
	
	@Column(name = "scenario_description", nullable = false, length = 100)
	private String scenarioDescription;
	
	@Column(name = "complete_exp", nullable = false)
	private Integer completeExp;
	
	@Column(name = "category", nullable = false, length = 50)
	private String category;
	
	public static Scenario create(String prompt,
							  String scenarioDescription,
							  Integer completeExp,
							  String category)
	{
		Scenario scenario = new Scenario();
		scenario.prompt = prompt;
		scenario.scenarioDescription = scenarioDescription;
		scenario.completeExp = completeExp;
		scenario.category = category;
		return scenario;
	}
	
	public void update(String prompt,
			   String scenarioDescription,
			   String category,
			   Integer completeExp) {
	
		this.prompt = prompt;
		this.scenarioDescription = scenarioDescription;
		this.category = category;
		this.completeExp = completeExp;
}
	
	// 테스트 코드 실행용
	public static Scenario withId(Long scenarioId) {
		Scenario scenario = new Scenario();
	
		scenario.scenarioId = scenarioId;
		
		return scenario;
	}

	// 테스트 코드 실행용
	public static Scenario testOf(long scenarioId, 
								  String prompt, 
								  String scenarioDescription, 
								  int completeExp, 
								  String category
								  ) {
		Scenario scenario = new Scenario();
		scenario.scenarioId = scenarioId;
		scenario.prompt = prompt;
		scenario.scenarioDescription =  scenarioDescription;
		scenario.completeExp = completeExp;
		scenario.category = category;
		
		return scenario;
	}

}
