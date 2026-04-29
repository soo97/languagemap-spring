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

import kr.co.mapspring.support.entity.Faq;
import kr.co.mapspring.user.entity.User;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "kr.co.mapspring.support.*"
    )
)
@ActiveProfiles("test")   // test 프로필 → H2 DB 사용
@DisplayName("FaqRepository 테스트")
class FaqRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private FaqRepository faqRepository;

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
    @DisplayName("Faq 저장 및 전체 조회")
    void faq_저장_및_전체_조회() {
        faqRepository.save(Faq.builder().user(user).question("질문1").answer("답변1").build());
        faqRepository.save(Faq.builder().user(user).question("질문2").answer("답변2").build());
        faqRepository.save(Faq.builder().user(user).question("질문3").answer("답변3").build());
        em.clear();

        List<Faq> result = faqRepository.findAll();

        assertThat(result).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Faq ID로 단건 조회")
    void faq_ID로_단건_조회() {
        Faq saved = faqRepository.save(
                Faq.builder().user(user).question("로그인 후 홈으로 돌아가나요?").answer("네, 홈으로 이동해요.").build()
        );
        em.clear();

        Faq found = faqRepository.findById(saved.getFaqId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getQuestion()).isEqualTo("로그인 후 홈으로 돌아가나요?");
        assertThat(found.getAnswer()).isEqualTo("네, 홈으로 이동해요.");
    }

    @Test
    @DisplayName("Faq 수정 후 조회")
    void faq_수정_후_조회() {
        Faq saved = faqRepository.save(
                Faq.builder().user(user).question("원래 질문").answer("원래 답변").build()
        );
        em.flush();

        saved.update("수정된 질문", "수정된 답변");
        faqRepository.save(saved);
        em.flush();
        em.clear();  // 1차 캐시 비우기

        Faq found = faqRepository.findById(saved.getFaqId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getQuestion()).isEqualTo("수정된 질문");
        assertThat(found.getAnswer()).isEqualTo("수정된 답변");
    }

    @Test
    @DisplayName("Faq 삭제 후 조회 시 빈 결과")
    void faq_삭제() {
        Faq saved = faqRepository.save(
                Faq.builder().user(user).question("삭제할 질문").answer("삭제할 답변").build()
        );
        em.flush();
        Long id = saved.getFaqId();

        faqRepository.deleteById(id);
        em.flush();
        em.clear();

        assertThat(faqRepository.findById(id)).isEmpty();
    }
}
