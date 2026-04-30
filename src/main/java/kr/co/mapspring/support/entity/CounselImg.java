package kr.co.mapspring.support.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "counsel_img")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounselImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsel_id", nullable = false)
    private Counsel counsel;

    @Column(name = "image", nullable = false, length = 100)
    private String image;

    @Builder
    private CounselImg(Counsel counsel, String image) {
        this.counsel = counsel;
        this.image = image;
    }
}