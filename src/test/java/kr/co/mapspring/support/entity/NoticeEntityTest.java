package kr.co.mapspring.support.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import kr.co.mapspring.user.entity.User;

@DataJpaTest(
	    includeFilters = @ComponentScan.Filter(
	        type = FilterType.REGEX,
	        pattern = "kr.co.mapspring.support.*"
	    )
	)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Notice 엔티티 테스트")
class NoticeEntityTest {

    @Autowired
    private TestEntityManager em;

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
    @DisplayName("Notice 저장 후 조회 성공")
    void notice_저장_후_조회() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("4월 업데이트 안내")
                .noticeKind(NoticeKind.UPDATE)
                .noticeText("지도 학습 플로우와 홈 화면이 개편되었어요.")
                .build();

        Notice saved = em.persistAndFlush(notice);
        em.clear();

        Notice found = em.find(Notice.class, saved.getNoticeId());

        assertThat(found).isNotNull();
        assertThat(found.getNoticeTitle()).isEqualTo("4월 업데이트 안내");
        assertThat(found.getNoticeKind()).isEqualTo(NoticeKind.UPDATE);
        assertThat(found.getNoticeText()).isEqualTo("지도 학습 플로우와 홈 화면이 개편되었어요.");
        assertThat(found.getNoticeDate()).isNotNull();     // @PrePersist 동작 확인
        assertThat(found.getNoticeChange()).isNull();      // 최초 저장 시 null
    }

    @Test
    @DisplayName("Notice 수정 후 noticeChange 자동 갱신")
    void notice_수정_후_noticeChange_갱신() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("원래 제목")
                .noticeKind(NoticeKind.CHECK)
                .noticeText("원래 내용")
                .build();

        em.persistAndFlush(notice);

        notice.update("수정된 제목", NoticeKind.EVENT, "수정된 내용", null);
        em.persistAndFlush(notice);
        em.clear();

        Notice found = em.find(Notice.class, notice.getNoticeId());

        assertThat(found.getNoticeTitle()).isEqualTo("수정된 제목");
        assertThat(found.getNoticeKind()).isEqualTo(NoticeKind.EVENT);
        assertThat(found.getNoticeChange()).isNotNull();  // @PreUpdate 동작 확인
    }

    @Test
    @DisplayName("NoticeKind 모든 유형 저장 가능")
    void notice_kind_모든_유형_저장() {
        for (NoticeKind kind : NoticeKind.values()) {
            Notice notice = Notice.builder()
                    .user(user)
                    .noticeTitle("공지 - " + kind.name())
                    .noticeKind(kind)
                    .noticeText("내용")
                    .build();

            Notice saved = em.persistAndFlush(notice);
            assertThat(saved.getNoticeKind()).isEqualTo(kind);
        }
    }

    @Test
    @DisplayName("Notice noticeTitle null 시 예외 발생")
    void notice_title_null_예외() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle(null)
                .noticeKind(NoticeKind.UPDATE)
                .noticeText("내용")
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(notice));
    }

    @Test
    @DisplayName("Notice noticeText null 시 예외 발생")
    void notice_text_null_예외() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("제목")
                .noticeKind(NoticeKind.UPDATE)
                .noticeText(null)
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(notice));
    }

    @Test
    @DisplayName("Notice noticeKind null 시 예외 발생")
    void notice_kind_null_예외() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("제목")
                .noticeKind(null)
                .noticeText("내용")
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(notice));
    }

    @Test
    @DisplayName("Notice noticeTitle 50자 초과 시 예외 발생")
    void notice_title_길이초과_예외() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("가".repeat(51))  // length = 50 초과
                .noticeKind(NoticeKind.UPDATE)
                .noticeText("내용")
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(notice));
    }

    @Test
    @DisplayName("Notice noticeText 500자 초과 시 예외 발생")
    void notice_text_길이초과_예외() {
        Notice notice = Notice.builder()
                .user(user)
                .noticeTitle("제목")
                .noticeKind(NoticeKind.UPDATE)
                .noticeText("가".repeat(501))  // length = 500 초과
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(notice));
    }
}
