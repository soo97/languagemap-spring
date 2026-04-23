package kr.co.mapspring.place.entity;

import java.time.LocalDateTime;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kr.co.mapspring.place.enums.LearningSessionStatus;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LearningSession {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "session_id", nullable = false, updatable = false)
	private Long sessionId;
	
	@Column(name = "start_time", nullable = false, updatable = false)
	private LocalDateTime startTime;
	
	@Column(name = "endTime")
	private LocalDateTime endTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id", nullable = false)   
	private Place place;
	
	@Column(name = "study_status", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private LearningSessionStatus studyStatus = LearningSessionStatus.READY;
	
	@PrePersist
	public void prePersist() {
		this.startTime = LocalDateTime.now();
	}
}
