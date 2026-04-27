package kr.co.mapspring.learning.entity;

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
import jakarta.persistence.Table;
import kr.co.mapspring.learning.enums.StudyType;
import kr.co.mapspring.place.entity.LearningSession;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_log_id", nullable = false, updatable = false)
    private Long studyLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private LearningSession learningSession;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type", nullable = false, length = 50)
    private StudyType studyType;

    @Column(name = "earned_exp", nullable = false)
    private Integer earnedExp;

    public static StudyLog create(User user, LearningSession learningSession, StudyType studyType, Integer earnedExp) {
        StudyLog studyLog = new StudyLog();
        studyLog.user = user;
        studyLog.learningSession = learningSession;
        studyLog.studyType = studyType;
        studyLog.earnedExp = earnedExp;
        return studyLog;
    }

    // 테스트 전용 메서드
    public static StudyLog of(Long studyLogId, User user, LearningSession learningSession, StudyType studyType, Integer earnedExp) {
        StudyLog studyLog = new StudyLog();
        studyLog.studyLogId = studyLogId;
        studyLog.user = user;
        studyLog.learningSession = learningSession;
        studyLog.studyType = studyType;
        studyLog.earnedExp = earnedExp;
        return studyLog;
    }
}
