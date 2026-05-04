package kr.co.mapspring.ai.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "content")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coaching_session_id", nullable = false)
    private CoachingSession coachingSession;

    @Column(name = "keyword", nullable = false, length = 255)
    private String keyword;

    @Column(name = "video_title", nullable = false, length = 255)
    private String videoTitle;

    @Column(name = "channel_title", nullable = false, length = 255)
    private String channelTitle;

    @Column(name = "thumbnail_url", nullable = false, length = 600)
    private String thumbnailUrl;

    @Column(name = "video_url", nullable = false, length = 500)
    private String videoUrl;

    @Column(name = "video_summary", columnDefinition = "TEXT")
    private String videoSummary;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Content create(
            CoachingSession session,
            String keyword,
            String title,
            String channel,
            String thumbnail,
            String videoUrl,
            String summary
    ) {
        Content content = new Content();
        content.coachingSession = session;
        content.keyword = keyword;
        content.videoTitle = title;
        content.channelTitle = channel;
        content.thumbnailUrl = thumbnail;
        content.videoUrl = videoUrl;
        content.videoSummary = summary;
        return content;
    }
}