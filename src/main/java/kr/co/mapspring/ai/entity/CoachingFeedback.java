package kr.co.mapspring.ai.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coaching_feedback")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoachingFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coaching_feedback_id", nullable = false, updatable = false)
    private Long coachingFeedbackId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coaching_session_id", nullable = false, unique = true)
    private CoachingSession coachingSession;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "summary_feedback", columnDefinition = "TEXT")
    private String summaryFeedback;

    @Column(name = "naturalness_level", length = 20)
    private String naturalnessLevel;

    @Column(name = "naturalness_comment", columnDefinition = "TEXT")
    private String naturalnessComment;

    @Column(name = "flow_level", length = 20)
    private String flowLevel;

    @Column(name = "flow_comment", columnDefinition = "TEXT")
    private String flowComment;

    @Column(name = "pronunciation_level", length = 20)
    private String pronunciationLevel;

    @Column(name = "pronunciation_comment", columnDefinition = "TEXT")
    private String pronunciationComment;

    @Column(name = "problem_words", columnDefinition = "TEXT")
    private String problemWords;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static CoachingFeedback create(
            CoachingSession coachingSession,
            Integer totalScore,
            String summaryFeedback,
            String naturalnessLevel,
            String naturalnessComment,
            String flowLevel,
            String flowComment,
            String pronunciationLevel,
            String pronunciationComment,
            String problemWords
    ) {
        CoachingFeedback feedback = new CoachingFeedback();
        feedback.coachingSession = coachingSession;
        feedback.totalScore = totalScore;
        feedback.summaryFeedback = summaryFeedback;
        feedback.naturalnessLevel = naturalnessLevel;
        feedback.naturalnessComment = naturalnessComment;
        feedback.flowLevel = flowLevel;
        feedback.flowComment = flowComment;
        feedback.pronunciationLevel = pronunciationLevel;
        feedback.pronunciationComment = pronunciationComment;
        feedback.problemWords = problemWords;
        return feedback;
    }
}