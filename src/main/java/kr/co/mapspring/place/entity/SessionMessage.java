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
import kr.co.mapspring.place.enums.SessionMessageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "session_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SessionMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id", nullable = false, updatable = false)
	private Long messageId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private LearningSession session;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_session_id", nullable = false)
	private MissionSession missionSession;
	
	@Column(name = "message", nullable = false, columnDefinition = "TEXT")
	private String message;
	
	@Column(name = "role", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private SessionMessageRole role;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
	
	public static SessionMessage create(LearningSession session,
										MissionSession runningMissionSession,
										String message,
										SessionMessageRole role
								 ) 
	{
		SessionMessage sessionMessage = new SessionMessage();
		sessionMessage.session = session;
		sessionMessage.missionSession = runningMissionSession;
		sessionMessage.message = message;
		sessionMessage.role = role;
		
		return sessionMessage;
	}

}
