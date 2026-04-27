package kr.co.mapspring.ai.entity;

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
import kr.co.mapspring.ai.enums.CoachingSessionStatus;
import kr.co.mapspring.place.entity.LearningSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coaching_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoachingSession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coaching_session_id", nullable = false, updatable = false)
	private Long coachingSessionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private LearningSession learningSession;

	@Column(name = "coaching_session_status", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private CoachingSessionStatus coachingSessionStatus = CoachingSessionStatus.READY;

	@Column(name = "final_feedback", columnDefinition = "TEXT")
	private String finalFeedback;

	@Column(name = "studied_at", nullable = false, updatable = false)
	private LocalDateTime studiedAt;

	@PrePersist
	public void prePersist() {
		this.studiedAt = LocalDateTime.now();
	}
	
	public static CoachingSession start(LearningSession learningSession) {
	    CoachingSession coachingSession = new CoachingSession();
	    coachingSession.learningSession = learningSession;
	    coachingSession.coachingSessionStatus = CoachingSessionStatus.RUNNING;
	    return coachingSession;
	}
}