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
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_profile_id", nullable = false, updatable = false)
    private Long learningProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "exp", nullable = false)
    private Integer exp = 0;

    @Column(name = "total_study_count", nullable = false)
    private Integer totalStudyCount = 0;


}
