package kr.co.mapspring.support.entity;

import jakarta.persistence.*;
import kr.co.mapspring.user.entity.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "counsel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counsel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counsel_id", nullable = false, updatable = false)
    private Long counselId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "counsel_name", nullable = false, length = 50)
    private String counselName;

    @CreationTimestamp
    @Column(name = "counsel_date", nullable = false, updatable = false)
    private LocalDateTime counselDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "counsel_kind", nullable = false, length = 50)
    private CounselKind counselKind;

    @Column(name = "counsel_text", nullable = false, length = 500)
    private String counselText;


    @Builder
    private Counsel(User user, String counselName, CounselKind counselKind,
                    String counselText) {
        this.user = user;
        this.counselName = counselName;
        this.counselKind = counselKind;
        this.counselText = counselText;
    }
}