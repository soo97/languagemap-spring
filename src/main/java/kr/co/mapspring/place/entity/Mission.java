package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_session")
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
	private String missionStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "scenario_id", nullable = false)
	private Scenario scenario;

}
