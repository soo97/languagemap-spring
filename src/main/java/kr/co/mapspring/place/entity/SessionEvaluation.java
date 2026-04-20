package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SessionEvaluation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long evaluationId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private LearningSession sessionId;
	
	@Column(name = "evaluation", nullable = false, columnDefinition = "TEXT")
	private String evaluation;

}
