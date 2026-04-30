package kr.co.mapspring.support.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import kr.co.mapspring.support.enums.NoticeKind;
import kr.co.mapspring.user.entity.User;

@DataJpaTest(
	    includeFilters = @ComponentScan.Filter(
	        type = FilterType.REGEX,
	        pattern = "kr.co.mapspring.support.*"
	    )
	)
@ActiveProfiles("test")   // test 프로필 → H2 DB 사용
@DisplayName("NoticeImg / NoticeUrl 엔티티 테스트")
class NoticeImgUrlEntityTest {

    @Autowired
    private TestEntityManager em;
 
    private Notice notice;
 
    @BeforeEach
    void setUp() {
        String unique = String.valueOf(System.currentTimeMillis());
 
        User user = User.create(
                "test" + unique + "@test.com",
                "홍길동",
                LocalDate.of(1990, 1, 1),
                "서울시 강남구",
                "010-" + unique.substring(unique.length() - 8),
                "hashedPassword"
        );
        em.persist(user);
 
        notice = Notice.builder()
                .user(user)
                .noticeTitle("공지 제목")
                .noticeKind(NoticeKind.UPDATE)
                .noticeText("공지 내용")
                .build();
        em.persistAndFlush(notice);
    }
 
    // ══════════════════════════════════════════════
    // NoticeImg 테스트
    // ══════════════════════════════════════════════
 
    @Test
    @DisplayName("NoticeImg 저장 후 조회 성공")
    void noticeImg_저장_후_조회() {
        NoticeImg img = NoticeImg.builder()
                .notice(notice)
                .image("images/test.jpg")
                .build();
 
        NoticeImg saved = em.persistAndFlush(img);
        em.clear();
 
        NoticeImg found = em.find(NoticeImg.class, saved.getImageId());
 
        assertThat(found).isNotNull();
        assertThat(found.getImage()).isEqualTo("images/test.jpg");
        assertThat(found.getNotice().getNoticeId()).isEqualTo(notice.getNoticeId());
    }
 
    @Test
    @DisplayName("NoticeImg 여러 장 저장 - 하나의 Notice에 다수 이미지")
    void noticeImg_다수_저장() {
        NoticeImg img1 = NoticeImg.builder().notice(notice).image("images/img1.jpg").build();
        NoticeImg img2 = NoticeImg.builder().notice(notice).image("images/img2.jpg").build();
        NoticeImg img3 = NoticeImg.builder().notice(notice).image("images/img3.jpg").build();
 
        em.persist(img1);
        em.persist(img2);
        em.persistAndFlush(img3);
 
        assertThat(img1.getImageId()).isNotNull();
        assertThat(img2.getImageId()).isNotNull();
        assertThat(img3.getImageId()).isNotNull();
        assertThat(img1.getImageId()).isNotEqualTo(img2.getImageId());
        assertThat(img2.getImageId()).isNotEqualTo(img3.getImageId());
    }
 
    @Test
    @DisplayName("NoticeImg image null 시 예외 발생")
    void noticeImg_image_null_예외() {
        NoticeImg img = NoticeImg.builder()
                .notice(notice)
                .image(null)
                .build();
 
        assertThrows(Exception.class, () -> {
            em.persist(img);
            em.flush(); 
        });
    }
 
    @Test
    @DisplayName("NoticeImg image 100자 초과 시 예외 발생")
    void noticeImg_image_길이초과_예외() {
        NoticeImg img = NoticeImg.builder()
                .notice(notice)
                .image("a".repeat(101))
                .build();
 
        assertThrows(Exception.class, () -> {
            em.persist(img);
            em.flush(); 
        });
    }
 
    @Test
    @DisplayName("NoticeImg notice null 시 예외 발생")
    void noticeImg_notice_null_예외() {
        NoticeImg img = NoticeImg.builder()
                .notice(null)
                .image("images/test.jpg")
                .build();
 
        assertThrows(Exception.class, () -> {
            em.persist(img);
            em.flush(); 
        });
    }
 
    // ══════════════════════════════════════════════
    // NoticeUrl 테스트
    // ══════════════════════════════════════════════
 
    @Test
    @DisplayName("NoticeUrl 저장 후 조회 성공")
    void noticeUrl_저장_후_조회() {
        NoticeUrl url = NoticeUrl.builder()
                .notice(notice)
                .link("https://example.com/notice")
                .build();
 
        NoticeUrl saved = em.persistAndFlush(url);
        em.clear();
 
        NoticeUrl found = em.find(NoticeUrl.class, saved.getUrlId());
 
        assertThat(found).isNotNull();
        assertThat(found.getLink()).isEqualTo("https://example.com/notice");
        assertThat(found.getNotice().getNoticeId()).isEqualTo(notice.getNoticeId());
    }
 
    @Test
    @DisplayName("NoticeUrl 여러 개 저장 - 하나의 Notice에 다수 URL")
    void noticeUrl_다수_저장() {
        NoticeUrl url1 = NoticeUrl.builder().notice(notice).link("https://url1.com").build();
        NoticeUrl url2 = NoticeUrl.builder().notice(notice).link("https://url2.com").build();
 
        em.persist(url1);
        em.persistAndFlush(url2);
 
        assertThat(url1.getUrlId()).isNotNull();
        assertThat(url2.getUrlId()).isNotNull();
        assertThat(url1.getUrlId()).isNotEqualTo(url2.getUrlId());
    }
 
    @Test
    @DisplayName("NoticeUrl link null 시 예외 발생")
    void noticeUrl_link_null_예외() {
        NoticeUrl url = NoticeUrl.builder()
                .notice(notice)
                .link(null)
                .build();
 
        assertThrows(Exception.class, () -> {
            em.persist(url);
            em.flush(); 
        });
    }
 
    @Test
    @DisplayName("NoticeUrl link 100자 초과 시 예외 발생")
    void noticeUrl_link_길이초과_예외() {
        NoticeUrl url = NoticeUrl.builder()
                .notice(notice)
                .link("https://" + "a".repeat(100))
                .build();
 
        assertThrows(Exception.class, () -> {
            em.persist(url);
            em.flush(); 
        });
    }
 
    @Test
    @DisplayName("NoticeUrl notice null 시 예외 발생")
    void noticeUrl_notice_null_예외() {
        NoticeUrl url = NoticeUrl.builder()
                .notice(null)
                .link("https://example.com")
                .build();
 
        assertThrows(Exception.class, () -> {
            em.persist(url);
            em.flush(); 
        });
    }
}
