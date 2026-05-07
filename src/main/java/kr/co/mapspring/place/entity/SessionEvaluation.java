package kr.co.mapspring.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "session_evaluation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SessionEvaluation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "evaluation_id", nullable = false, updatable = false)
	private Long evaluationId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private LearningSession session;
	
	@Column(name = "evaluation", nullable = false, columnDefinition = "TEXT")
	private String evaluation;
	
	public static SessionEvaluation create (LearningSession learningSession, 
											String evaluation) 
	{
		SessionEvaluation sessionEvaluation = new SessionEvaluation();
		sessionEvaluation.session = learningSession;
		sessionEvaluation.evaluation = evaluation;
		
		return sessionEvaluation;
	}

}
