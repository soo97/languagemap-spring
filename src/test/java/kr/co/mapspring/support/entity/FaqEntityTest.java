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
@DisplayName("Faq 엔티티 테스트")
class FaqEntityTest {

    @Autowired
    private TestEntityManager em;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create(
                "test@test.com",
                "홍길동",
                LocalDate.of(1990, 1, 1),
                "서울시 강남구",
                "010-1234-5678",
                "hashedPassword"
        );
        em.persist(user);
    }

    @Test
    @DisplayName("Faq 저장 후 조회 성공")
    void faq_저장_후_조회() {
        Faq faq = Faq.builder()
                .user(user)
                .question("로그인 후 홈으로 돌아가나요?")
                .answer("네. 시연용 플로우에서는 로그인 완료 후 홈 화면으로 이동해요.")
                .build();

        Faq saved = em.persistAndFlush(faq);
        em.clear();

        Faq found = em.find(Faq.class, saved.getFaqId());

        assertThat(found).isNotNull();
        assertThat(found.getQuestion()).isEqualTo("로그인 후 홈으로 돌아가나요?");
        assertThat(found.getAnswer()).isEqualTo("네. 시연용 플로우에서는 로그인 완료 후 홈 화면으로 이동해요.");
    }

    @Test
    @DisplayName("Faq update 메서드로 수정 성공")
    void faq_수정_성공() {
        Faq faq = Faq.builder()
                .user(user)
                .question("원래 질문")
                .answer("원래 답변")
                .build();

        em.persistAndFlush(faq);

        faq.update("수정된 질문", "수정된 답변");
        em.persistAndFlush(faq);
        em.clear();

        Faq found = em.find(Faq.class, faq.getFaqId());

        assertThat(found.getQuestion()).isEqualTo("수정된 질문");
        assertThat(found.getAnswer()).isEqualTo("수정된 답변");
    }

    @Test
    @DisplayName("Faq question null 시 예외 발생")
    void faq_question_null_예외() {
        Faq faq = Faq.builder()
                .user(user)
                .question(null)
                .answer("답변")
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(faq));
    }

    @Test
    @DisplayName("Faq answer null 시 예외 발생")
    void faq_answer_null_예외() {
        Faq faq = Faq.builder()
                .user(user)
                .question("질문")
                .answer(null)
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(faq));
    }

    @Test
    @DisplayName("Faq question 100자 초과 시 예외 발생")
    void faq_question_길이초과_예외() {
        Faq faq = Faq.builder()
                .user(user)
                .question("가".repeat(101)) // length = 100 초과
                .answer("답변")
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(faq));
    }

    @Test
    @DisplayName("Faq answer 500자 초과 시 예외 발생")
    void faq_answer_길이초과_예외() {
        Faq faq = Faq.builder()
                .user(user)
                .question("질문")
                .answer("가".repeat(501)) // length = 500 초과
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(faq));
    }

    @Test
    @DisplayName("Faq user null 시 예외 발생")
    void faq_user_null_예외() {
        Faq faq = Faq.builder()
                .user(null)
                .question("질문")
                .answer("답변")
                .build();

        assertThrows(Exception.class, () -> em.persistAndFlush(faq));
    }
}
