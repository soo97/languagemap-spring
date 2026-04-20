package kr.co.mapspring.learning.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LearningProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_profile_id")
    private Long learningProfileId;

    //    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // fk

    @Builder.Default
    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Builder.Default
    @Column(name = "exp", nullable = false)
    private Integer exp = 0;

    @Builder.Default
    @Column(name = "total_study_count", nullable = false)
    private Integer totalStudyCount = 0;


}
