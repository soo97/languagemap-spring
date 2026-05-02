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
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coaching_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoachingMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coaching_message_id", nullable = false, updatable = false)
	private Long coachingMessageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coaching_session_id", nullable = false)
	private CoachingSession coachingSession;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coaching_script_turn_id")
	private CoachingScriptTurn coachingScriptTurn;

	@Column(name = "role", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private CoachingMessageRole role;

	@Column(name = "message", nullable = true, columnDefinition = "TEXT")
	private String message;
	
	@Column(name = "audio_url", length = 600)
	private String audioUrl;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
	
	public static CoachingMessage create(
	        CoachingSession coachingSession,
	        CoachingScriptTurn coachingScriptTurn,
	        CoachingMessageRole role,
	        String message,
	        String audioUrl
	) {
	    CoachingMessage coachingMessage = new CoachingMessage();
	    coachingMessage.coachingSession = coachingSession;
	    coachingMessage.coachingScriptTurn = coachingScriptTurn;
	    coachingMessage.role = role;
	    coachingMessage.message = message;
	    coachingMessage.audioUrl = audioUrl;
	    return coachingMessage;
	}
}
