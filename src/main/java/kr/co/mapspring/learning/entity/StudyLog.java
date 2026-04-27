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

//    TODO: 추후 User 엔티티 확정 시 연관관계로 매핑 예정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
//    @Column(name = "user_id", nullable = false)
    private Long userId;

//    TODO: 추후 Session 엔티티 확정 시 연관관계로 매핑 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type", nullable = false, length = 50)
    private StudyType studyType;

    @Column(name = "earned_exp", nullable = false)
    private Integer earnedExp;

    public static StudyLog create(Long userId, Long sessionId, StudyType studyType, Integer earnedExp) {
        StudyLog studyLog = new StudyLog();
        studyLog.userId = userId;
        studyLog.sessionId = sessionId;
        studyLog.studyType = studyType;
        studyLog.earnedExp = earnedExp;
        return studyLog;
    }

    // 테스트 전용 메서드
    public static StudyLog of(Long studyLogId, Long userId, Long sessionId, StudyType studyType, Integer earnedExp) {
        StudyLog studyLog = new StudyLog();
        studyLog.studyLogId = studyLogId;
        studyLog.userId = userId;
        studyLog.sessionId = sessionId;
        studyLog.studyType = studyType;
        studyLog.earnedExp = earnedExp;
        return studyLog;
    }
}
