package kr.co.mapspring.support.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import kr.co.mapspring.support.enums.CounselKind;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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