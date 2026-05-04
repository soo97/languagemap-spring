package kr.co.mapspring.place.entity;

import java.time.LocalDateTime;

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
import kr.co.mapspring.place.enums.MissionSessionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mission_session")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_session_id", nullable = false, updatable = false)
    private Long missionSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private LearningSession learningSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "mission_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MissionSessionStatus missionStatus;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    public static MissionSession create(LearningSession saveLearningSession, 
    										  Mission mission) {
    	MissionSession missionSession = new MissionSession();
    	missionSession.learningSession = saveLearningSession;
    	missionSession.mission = mission;
    	missionSession.missionStatus = MissionSessionStatus.READY;
    	
    	return missionSession;
    }
    
    public void start() {
    	this.startedAt = LocalDateTime.now();
    	this.missionStatus = MissionSessionStatus.RUNNING;
    }
    
    public void complete() {
        this.missionStatus = MissionSessionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}

