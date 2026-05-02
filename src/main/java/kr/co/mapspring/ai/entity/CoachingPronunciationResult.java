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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coaching_pronunciation_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoachingPronunciationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pronunciation_result_id", nullable = false, updatable = false)
    private Long pronunciationResultId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coaching_message_id", nullable = false, unique = true)
    private CoachingMessage coachingMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coaching_script_turn_id", nullable = false)
    private CoachingScriptTurn coachingScriptTurn;

    @Column(name = "recognized_text", nullable = false, columnDefinition = "TEXT")
    private String recognizedText;

    @Column(name = "accuracy_score")
    private Double accuracyScore;

    @Column(name = "fluency_score")
    private Double fluencyScore;

    @Column(name = "completeness_score")
    private Double completenessScore;

    @Column(name = "pronunciation_score")
    private Double pronunciationScore;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "problem_words", columnDefinition = "TEXT")
    private String problemWords;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static CoachingPronunciationResult create(
            CoachingMessage coachingMessage,
            CoachingScriptTurn coachingScriptTurn,
            String recognizedText,
            Double accuracyScore,
            Double fluencyScore,
            Double completenessScore,
            Double pronunciationScore,
            String feedback,
            String problemWords
    ) {
        CoachingPronunciationResult result = new CoachingPronunciationResult();
        result.coachingMessage = coachingMessage;
        result.coachingScriptTurn = coachingScriptTurn;
        result.recognizedText = recognizedText;
        result.accuracyScore = accuracyScore;
        result.fluencyScore = fluencyScore;
        result.completenessScore = completenessScore;
        result.pronunciationScore = pronunciationScore;
        result.feedback = feedback;
        result.problemWords = problemWords;
        return result;
    }
}