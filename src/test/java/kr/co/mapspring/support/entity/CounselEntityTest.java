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

import kr.co.mapspring.support.enums.CounselKind;
import kr.co.mapspring.user.entity.User;

@DataJpaTest(
	    includeFilters = @ComponentScan.Filter(
	        type = FilterType.REGEX,
	        pattern = "kr.co.mapspring.support.*"
	    )
	)
@ActiveProfiles("test")   // test 프로필 → H2 DB 사용
@DisplayName("Counsel / CounselAnswer / CounselImg 엔티티 테스트")
class CounselEntityTest {

    @Autowired
    private TestEntityManager em;
 
    private User user;
    private Counsel counsel;
 
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
 
        counsel = Counsel.builder()
                .user(user)
                .counselName("로그인 후 홈 이동 여부")
                .counselKind(CounselKind.LOGIN_SIGNUP)
                .counselText("로그인 완료 후 홈으로 이동하는 플로우를 확인하고 싶어요.")
                .build();
        em.persistAndFlush(counsel);
    }
 
    // ══════════════════════════════════════════════
    // Counsel 테스트
    // ══════════════════════════════════════════════
 
    @Test
    @DisplayName("Counsel 저장 후 조회 성공")
    void counsel_저장_후_조회() {
        em.clear();
 
        Counsel found = em.find(Counsel.class, counsel.getCounselId());
 
        assertThat(found).isNotNull();
        assertThat(found.getCounselName()).isEqualTo("로그인 후 홈 이동 여부");
        assertThat(found.getCounselKind()).isEqualTo(CounselKind.LOGIN_SIGNUP);
        assertThat(found.getCounselText()).isEqualTo("로그인 완료 후 홈으로 이동하는 플로우를 확인하고 싶어요.");
        assertThat(found.getCounselDate()).isNotNull();
    }
 
    @Test
    @DisplayName("CounselKind 모든 유형 저장 가능")
    void counsel_kind_모든_유형_저장() {
        for (CounselKind kind : CounselKind.values()) {
            Counsel c = Counsel.builder()
                    .user(user)
                    .counselName("문의 - " + kind.name())
                    .counselKind(kind)
                    .counselText("내용")
                    .build();
 
            Counsel saved = em.persistAndFlush(c);
            assertThat(saved.getCounselKind()).isEqualTo(kind);
        }
    }
 
    @Test
    @DisplayName("Counsel counselName null 시 예외 발생")
    void counsel_name_null_예외() {
        Counsel c = Counsel.builder()
                .user(user)
                .counselName(null)
                .counselKind(CounselKind.OTHER)
                .counselText("내용")
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(c));
    }
 
    @Test
    @DisplayName("Counsel counselText null 시 예외 발생")
    void counsel_text_null_예외() {
        Counsel c = Counsel.builder()
                .user(user)
                .counselName("제목")
                .counselKind(CounselKind.OTHER)
                .counselText(null)
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(c));
    }
 
    @Test
    @DisplayName("Counsel counselName 50자 초과 시 예외 발생")
    void counsel_name_길이초과_예외() {
        Counsel c = Counsel.builder()
                .user(user)
                .counselName("가".repeat(51))
                .counselKind(CounselKind.OTHER)
                .counselText("내용")
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(c));
    }
 
    @Test
    @DisplayName("Counsel counselText 500자 초과 시 예외 발생")
    void counsel_text_길이초과_예외() {
        Counsel c = Counsel.builder()
                .user(user)
                .counselName("제목")
                .counselKind(CounselKind.OTHER)
                .counselText("가".repeat(501))
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(c));
    }
 
    // ══════════════════════════════════════════════
    // CounselAnswer 테스트
    // ══════════════════════════════════════════════
 
    @Test
    @DisplayName("CounselAnswer 저장 후 조회 성공")
    void counselAnswer_저장_후_조회() {
        CounselAnswer answer = CounselAnswer.builder()
                .user(user)
                .counsel(counsel)
                .answer("안녕하세요. 로그인 완료 후 홈 화면으로 이동합니다.")
                .build();
 
        CounselAnswer saved = em.persistAndFlush(answer);
        em.clear();
 
        CounselAnswer found = em.find(CounselAnswer.class, saved.getAnswerId());
 
        assertThat(found).isNotNull();
        assertThat(found.getAnswer()).isEqualTo("안녕하세요. 로그인 완료 후 홈 화면으로 이동합니다.");
        assertThat(found.getCounsel().getCounselId()).isEqualTo(counsel.getCounselId());
    }
 
    @Test
    @DisplayName("CounselAnswer answer null 시 예외 발생")
    void counselAnswer_answer_null_예외() {
        CounselAnswer answer = CounselAnswer.builder()
                .user(user)
                .counsel(counsel)
                .answer(null)
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(answer));
    }
 
    @Test
    @DisplayName("CounselAnswer answer 100자 초과 시 예외 발생")
    void counselAnswer_answer_길이초과_예외() {
        CounselAnswer answer = CounselAnswer.builder()
                .user(user)
                .counsel(counsel)
                .answer("가".repeat(101))
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(answer));
    }
 
    @Test
    @DisplayName("CounselAnswer counsel null 시 예외 발생")
    void counselAnswer_counsel_null_예외() {
        CounselAnswer answer = CounselAnswer.builder()
                .user(user)
                .counsel(null)
                .answer("답변 내용")
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(answer));
    }
 
    // ══════════════════════════════════════════════
    // CounselImg 테스트
    // ══════════════════════════════════════════════
 
    @Test
    @DisplayName("CounselImg 저장 후 조회 성공")
    void counselImg_저장_후_조회() {
        CounselImg img = CounselImg.builder()
                .counsel(counsel)
                .image("images/counsel.jpg")
                .build();
 
        CounselImg saved = em.persistAndFlush(img);
        em.clear();
 
        CounselImg found = em.find(CounselImg.class, saved.getImageId());
 
        assertThat(found).isNotNull();
        assertThat(found.getImage()).isEqualTo("images/counsel.jpg");
        assertThat(found.getCounsel().getCounselId()).isEqualTo(counsel.getCounselId());
    }
 
    @Test
    @DisplayName("CounselImg image null 시 예외 발생")
    void counselImg_image_null_예외() {
        CounselImg img = CounselImg.builder()
                .counsel(counsel)
                .image(null)
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(img));
    }
 
    @Test
    @DisplayName("CounselImg image 100자 초과 시 예외 발생")
    void counselImg_image_길이초과_예외() {
        CounselImg img = CounselImg.builder()
                .counsel(counsel)
                .image("a".repeat(101))
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(img));
    }
 
    @Test
    @DisplayName("CounselImg counsel null 시 예외 발생")
    void counselImg_counsel_null_예외() {
        CounselImg img = CounselImg.builder()
                .counsel(null)
                .image("images/counsel.jpg")
                .build();
 
        assertThrows(DataIntegrityViolationException.class, () -> em.persistAndFlush(img));
    }
}
