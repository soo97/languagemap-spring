package kr.co.mapspring.support.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import jakarta.transaction.Transactional;
import kr.co.mapspring.support.entity.Notice;
import kr.co.mapspring.support.enums.NoticeKind;
import kr.co.mapspring.user.entity.User;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "kr.co.mapspring.support.*"
    )
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("NoticeRepository 테스트")
class NoticeRepositoryTest {
	
	@Autowired
	private NoticeImgRepository noticeImgRepository;

	@Autowired
	private NoticeUrlRepository noticeUrlRepository;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private NoticeRepository noticeRepository;

    private User user;

    @BeforeEach
    void setUp() {
        String unique = String.valueOf(System.currentTimeMillis());

        user = User.create(
                "test" + unique + "@test.com",
                "홍길동",
                LocalDate.of(1990, 1, 1),
                "서울시 강남구",
                "010-" + unique.substring(unique.length() - 8),
                "hashedPassword"
        );
        em.persist(user);
    }

    @Test
    @DisplayName("Notice 저장 및 ID 조회 성공")
    void notice_저장_및_ID_조회() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("공지 제목")
                .noticeKind(NoticeKind.UPDATE)
                .noticeText("공지 내용")
                .build();

        Notice saved = noticeRepository.save(notice);
        em.clear();

        Notice found = noticeRepository.findById(saved.getNoticeId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getNoticeTitle()).isEqualTo("공지 제목");
        assertThat(found.getNoticeKind()).isEqualTo(NoticeKind.UPDATE);
    }

    @Test
    @DisplayName("findAllByOrderByNoticeDateDesc - 최신순 정렬 확인")
    void 전체_목록_최신순_조회() throws InterruptedException {
        Notice notice1 = Notice.builder()
                .user(user).noticeTitle("첫 번째 공지").noticeKind(NoticeKind.UPDATE).noticeText("내용1").build();
        noticeRepository.save(notice1);

        Thread.sleep(10); // 시간 차이 생성

        Notice notice2 = Notice.builder()
                .user(user).noticeTitle("두 번째 공지").noticeKind(NoticeKind.CHECK).noticeText("내용2").build();
        noticeRepository.save(notice2);

        em.clear();

        List<Notice> result = noticeRepository.findAllByOrderByNoticeDateDesc();

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        // 두 번째 공지가 더 최신이므로 앞에 있어야 함
        assertThat(result.get(0).getNoticeTitle()).isEqualTo("두 번째 공지");
    }

    @Test
    @DisplayName("findByNoticeKindOrderByNoticeDateDesc - 유형별 필터링 확인")
    void 유형별_목록_조회() {
        noticeRepository.save(Notice.builder()
                .user(user).noticeTitle("업데이트 공지1").noticeKind(NoticeKind.UPDATE).noticeText("내용").build());
        noticeRepository.save(Notice.builder()
                .user(user).noticeTitle("업데이트 공지2").noticeKind(NoticeKind.UPDATE).noticeText("내용").build());
        noticeRepository.save(Notice.builder()
                .user(user).noticeTitle("점검 공지").noticeKind(NoticeKind.CHECK).noticeText("내용").build());

        em.clear();

        List<Notice> updateNotices = noticeRepository.findByNoticeKindOrderByNoticeDateDesc(NoticeKind.UPDATE);
        List<Notice> checkNotices = noticeRepository.findByNoticeKindOrderByNoticeDateDesc(NoticeKind.CHECK);
        List<Notice> eventNotices = noticeRepository.findByNoticeKindOrderByNoticeDateDesc(NoticeKind.EVENT);

        assertThat(updateNotices).allMatch(n -> n.getNoticeKind() == NoticeKind.UPDATE);
        assertThat(checkNotices).allMatch(n -> n.getNoticeKind() == NoticeKind.CHECK);
        assertThat(eventNotices).allMatch(n -> n.getNoticeKind() == NoticeKind.EVENT);
    }

    @Test
    @Transactional
    @DisplayName("Notice 삭제 성공")
    void notice_삭제() {
        Notice notice = Notice.builder()
                .user(user).noticeTitle("삭제할 공지").noticeKind(NoticeKind.EVENT).noticeText("내용").build();

        Notice saved = noticeRepository.save(notice);
        em.flush();
        Long id = saved.getNoticeId();

        // 연관 데이터 먼저 삭제
        noticeImgRepository.deleteByNoticeNoticeId(id);
        noticeUrlRepository.deleteByNoticeNoticeId(id);
        em.flush();

        noticeRepository.deleteById(id);
        em.flush();
        em.clear();

        assertThat(noticeRepository.findById(id)).isEmpty();
    }
}
