package kr.co.mapspring.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.mapspring.ai.entity.CoachingSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "content_id", nullable = false, updatable = false)
	private Long contentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coaching_session_id", nullable = false)
	private CoachingSession coachingSession;

	@Column(name = "video_title")
	private String videoTitle;

	@Column(name = "video_url", length = 500)
	private String videoUrl;

	@Column(name = "video_summary", columnDefinition = "TEXT")
	private String videoSummary;
}