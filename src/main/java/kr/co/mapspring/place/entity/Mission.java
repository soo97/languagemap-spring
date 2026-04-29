package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.mapspring.place.enums.MissionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Mission {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "mission_id", nullable = false, updatable = false)
	private Long missionId;
	
	@Column(name = "mission_title", nullable = false, length = 100)
	private String missionTitle;
	
	@Column(name = "mission_description", nullable = false)
	private String missionDescription;
	
	@Column(name = "mission_status", nullable = false, length = 50)
	@Enumerated(EnumType.STRING)
	private MissionStatus missionStatus = MissionStatus.READY;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scenario_id", nullable = false)
	private Scenario scenario;

	public static Mission create(String missionTitle, 
								 String missionDescription, 
								 Scenario scenario) {
		
		Mission mission = new Mission();
		mission.missionTitle = missionTitle;
		mission.missionDescription = missionDescription;
		mission.scenario = scenario;
		
		return mission;
	}

	public void update(String missionTitle,
								 String missionDescription,
								 Scenario scenario)
	{
		this.missionTitle = missionTitle;
		this.missionDescription = missionDescription;
		this.scenario = scenario;
	}

}
