package kr.co.mapspring.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.mapspring.learning.enums.StudyType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_log_id", nullable = false, updatable = false)
    private Long studyLogId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "user_id", nullable = false)
    private Long userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "place_id", nullable = false)
    @Column(name = "place_id", nullable = false)
    private Long placeId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "scenario_id", nullable = false)
    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type", nullable = false, length = 50)
    private StudyType studyType;

    @Column(name = "pronunciation_score")
    private Integer pronunciationScore;

    @Column(name = "naturalness_score")
    private Integer naturalnessScore;

    @Column(name = "fluency_score")
    private Integer fluencyScore;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "earned_exp", nullable = false)
    private Integer earnedExp;

    @Column(name = "study_duration_sec", nullable = false)
    private Integer studyDurationSec;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


}
