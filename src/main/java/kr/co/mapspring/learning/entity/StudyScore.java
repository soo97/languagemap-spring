package kr.co.mapspring.learning.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_score")
@Getter
@NoArgsConstructor
public class StudyScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_score_id", nullable = false, updatable = false)
    private Long studyScoreId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_log_id", nullable = false, unique = true)
    private StudyLog studyLog;

    @Column(name = "naturalness_score")
    private Integer naturalnessScore;

    @Column(name = "fluency_score")
    private Integer fluencyScore;

    @Column(name = "total_score")
    private Integer totalScore;

    public static StudyScore create(StudyLog studyLog, Integer naturalnessScore, Integer fluencyScore, Integer totalScore) {
        StudyScore studyScore = new StudyScore();
        studyScore.studyLog = studyLog;
        studyScore.naturalnessScore = naturalnessScore;
        studyScore.fluencyScore = fluencyScore;
        studyScore.totalScore = totalScore;
        return studyScore;
    }

    // 테스트 전용 메서드
    public static StudyScore of(Long studyScoreId, StudyLog studyLog, Integer naturalnessScore, Integer fluencyScore, Integer totalScore) {
        StudyScore studyScore = new StudyScore();
        studyScore.studyScoreId = studyScoreId;
        studyScore.studyLog = studyLog;
        studyScore.naturalnessScore = naturalnessScore;
        studyScore.fluencyScore = fluencyScore;
        studyScore.totalScore = totalScore;
        return studyScore;
    }
}
