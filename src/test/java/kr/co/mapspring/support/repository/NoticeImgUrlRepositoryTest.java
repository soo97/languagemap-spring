package kr.co.mapspring.support.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import kr.co.mapspring.support.entity.Notice;
import kr.co.mapspring.support.entity.NoticeImg;
import kr.co.mapspring.support.entity.NoticeUrl;
import kr.co.mapspring.support.enums.NoticeKind;
import kr.co.mapspring.user.entity.User;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "kr.co.mapspring.support.*"
    )
)
@ActiveProfiles("test")   // test 프로필 → H2 DB 사용
@DisplayName("NoticeImgRepository / NoticeUrlRepository 테스트")
class NoticeImgUrlRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private NoticeImgRepository noticeImgRepository;

    @Autowired
    private NoticeUrlRepository noticeUrlRepository;

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
    // NoticeImgRepository 테스트
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("NoticeImg 저장 후 noticeId로 목록 조회")
    void noticeImg_noticeId로_조회() {
        noticeImgRepository.save(NoticeImg.builder().notice(notice).image("images/img1.jpg").build());
        noticeImgRepository.save(NoticeImg.builder().notice(notice).image("images/img2.jpg").build());
        em.clear();

        List<NoticeImg> result = noticeImgRepository.findByNoticeNoticeId(notice.getNoticeId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(img -> img.getNotice().getNoticeId().equals(notice.getNoticeId()));
    }

    @Test
    @DisplayName("NoticeImg 없는 noticeId 조회 시 빈 리스트 반환")
    void noticeImg_없는_noticeId_조회() {
        List<NoticeImg> result = noticeImgRepository.findByNoticeNoticeId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("NoticeImg noticeId로 전체 삭제")
    void noticeImg_noticeId로_전체_삭제() {
        noticeImgRepository.save(NoticeImg.builder().notice(notice).image("images/img1.jpg").build());
        noticeImgRepository.save(NoticeImg.builder().notice(notice).image("images/img2.jpg").build());
        em.flush();
        em.clear();

        noticeImgRepository.deleteByNoticeNoticeId(notice.getNoticeId());
        em.flush();
        em.clear();

        List<NoticeImg> result = noticeImgRepository.findByNoticeNoticeId(notice.getNoticeId());
        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════
    // NoticeUrlRepository 테스트
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("NoticeUrl 저장 후 noticeId로 목록 조회")
    void noticeUrl_noticeId로_조회() {
        noticeUrlRepository.save(NoticeUrl.builder().notice(notice).link("https://url1.com").build());
        noticeUrlRepository.save(NoticeUrl.builder().notice(notice).link("https://url2.com").build());
        em.clear();

        List<NoticeUrl> result = noticeUrlRepository.findByNoticeNoticeId(notice.getNoticeId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(url -> url.getNotice().getNoticeId().equals(notice.getNoticeId()));
    }

    @Test
    @DisplayName("NoticeUrl 없는 noticeId 조회 시 빈 리스트 반환")
    void noticeUrl_없는_noticeId_조회() {
        List<NoticeUrl> result = noticeUrlRepository.findByNoticeNoticeId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("NoticeUrl noticeId로 전체 삭제")
    void noticeUrl_noticeId로_전체_삭제() {
        noticeUrlRepository.save(NoticeUrl.builder().notice(notice).link("https://url1.com").build());
        noticeUrlRepository.save(NoticeUrl.builder().notice(notice).link("https://url2.com").build());
        em.flush();
        em.clear();

        noticeUrlRepository.deleteByNoticeNoticeId(notice.getNoticeId());
        em.flush();
        em.clear();

        List<NoticeUrl> result = noticeUrlRepository.findByNoticeNoticeId(notice.getNoticeId());
        assertThat(result).isEmpty();
    }
}
