package kr.co.mapspring.ai.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coaching_script_turn")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CoachingScriptTurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coaching_script_turn_id", nullable = false, updatable = false)
    private Long coachingScriptTurnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coaching_session_id", nullable = false)
    private CoachingSession coachingSession;

    @Column(name = "turn_order", nullable = false)
    private Integer turnOrder;

    @Column(name = "assistant_text", nullable = false, columnDefinition = "TEXT")
    private String assistantText;

    @Column(name = "expected_text", nullable = false, columnDefinition = "TEXT")
    private String expectedText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static CoachingScriptTurn create(
            CoachingSession coachingSession,
            Integer turnOrder,
            String assistantText,
            String expectedText
    ) {
        CoachingScriptTurn coachingScriptTurn = new CoachingScriptTurn();
        coachingScriptTurn.coachingSession = coachingSession;
        coachingScriptTurn.turnOrder = turnOrder;
        coachingScriptTurn.assistantText = assistantText;
        coachingScriptTurn.expectedText = expectedText;
        return coachingScriptTurn;
    }
}