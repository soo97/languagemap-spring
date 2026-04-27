package kr.co.mapspring.support.entity;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false, updatable = false)
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "notice_title", nullable = false, length = 50)
    private String noticeTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_kind", nullable = false, length = 50)
    private NoticeKind noticeKind;

    @Column(name = "notice_date", nullable = false, updatable = false)
    private LocalDateTime noticeDate;

    @Column(name = "notice_change")
    private LocalDateTime noticeChange;

    @Column(name = "notice_text", nullable = false, length = 500)
    private String noticeText;


    // 공지 수정 메서드
    public void update(String noticeTitle, NoticeKind noticeKind,
                       String noticeText, String noticeUrl) {
        this.noticeTitle = noticeTitle;
        this.noticeKind = noticeKind;
        this.noticeText = noticeText;
    }

    @Builder
    private Notice(User user, String noticeTitle, NoticeKind noticeKind,
                   String noticeText, String noticeUrl) {
        this.user = user;
        this.noticeTitle = noticeTitle;
        this.noticeKind = noticeKind;
        this.noticeText = noticeText;
    }
    
    @PrePersist
    protected void prePersist() {
        this.noticeDate = LocalDateTime.now();
        this.noticeChange = null;
    }
	
	@PreUpdate
	protected void preUpdate() {
	    this.noticeChange = LocalDateTime.now();
	}
}