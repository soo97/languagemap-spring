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

import kr.co.mapspring.support.entity.Counsel;
import kr.co.mapspring.support.entity.CounselAnswer;
import kr.co.mapspring.support.entity.CounselImg;
import kr.co.mapspring.support.enums.CounselKind;
import kr.co.mapspring.user.entity.User;

@DataJpaTest(
    includeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "kr.co.mapspring.support.*"
    )
)
@ActiveProfiles("test")   // test 프로필 → H2 DB 사용
@DisplayName("CounselRepository / CounselAnswerRepository / CounselImgRepository 테스트")
class CounselRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CounselRepository counselRepository;

    @Autowired
    private CounselAnswerRepository counselAnswerRepository;

    @Autowired
    private CounselImgRepository counselImgRepository;

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
    // CounselRepository 테스트
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("Counsel 저장 및 ID 조회")
    void counsel_저장_및_ID_조회() {
        em.clear();

        Counsel found = counselRepository.findById(counsel.getCounselId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getCounselName()).isEqualTo("로그인 후 홈 이동 여부");
        assertThat(found.getCounselKind()).isEqualTo(CounselKind.LOGIN_SIGNUP);
    }

    @Test
    @DisplayName("findByUserUserIdOrderByCounselDateDesc - 특정 유저 문의 최신순 조회")
    void 유저별_문의_최신순_조회() throws InterruptedException {
        Thread.sleep(10);

        Counsel counsel2 = Counsel.builder()
                .user(user)
                .counselName("두 번째 문의")
                .counselKind(CounselKind.LEARNING)
                .counselText("내용")
                .build();
        counselRepository.save(counsel2);
        em.clear();

        List<Counsel> result = counselRepository.findByUserUserIdOrderByCounselDateDesc(user.getUserId());

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        assertThat(result.get(0).getCounselName()).isEqualTo("두 번째 문의");
        assertThat(result).allMatch(c -> c.getUser().getUserId().equals(user.getUserId()));
    }

    @Test
    @DisplayName("다른 유저 문의는 조회되지 않음")
    void 다른_유저_문의_조회_안됨() {
        String unique2 = String.valueOf(System.currentTimeMillis() + 1);
        User otherUser = User.create(
                "other" + unique2 + "@test.com",
                "김철수",
                LocalDate.of(1995, 5, 5),
                "부산시 해운대구",
                "010-" + unique2.substring(unique2.length() - 8),
                "hashedPassword"
        );
        em.persist(otherUser);

        counselRepository.save(Counsel.builder()
                .user(otherUser)
                .counselName("다른 유저 문의")
                .counselKind(CounselKind.OTHER)
                .counselText("내용")
                .build());
        em.clear();

        List<Counsel> result = counselRepository.findByUserUserIdOrderByCounselDateDesc(user.getUserId());

        assertThat(result).allMatch(c -> c.getUser().getUserId().equals(user.getUserId()));
    }

    @Test
    @DisplayName("Counsel 삭제")
    void counsel_삭제() {
        Long id = counsel.getCounselId();

        // 연관 데이터 먼저 삭제
        counselAnswerRepository.deleteByCounselCounselId(id);
        counselImgRepository.deleteByCounselCounselId(id);
        em.flush();

        counselRepository.deleteById(id);
        em.flush();
        em.clear();

        assertThat(counselRepository.findById(id)).isEmpty();
    }

    // ══════════════════════════════════════════════
    // CounselAnswerRepository 테스트
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("CounselAnswer 저장 후 counselId로 목록 조회")
    void counselAnswer_counselId로_조회() {
        counselAnswerRepository.save(CounselAnswer.builder()
                .user(user).counsel(counsel).answer("첫 번째 답변").build());
        counselAnswerRepository.save(CounselAnswer.builder()
                .user(user).counsel(counsel).answer("두 번째 답변").build());
        em.clear();

        List<CounselAnswer> result = counselAnswerRepository.findByCounselCounselId(counsel.getCounselId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a -> a.getCounsel().getCounselId().equals(counsel.getCounselId()));
    }

    @Test
    @DisplayName("CounselAnswer 없는 counselId 조회 시 빈 리스트")
    void counselAnswer_없는_counselId_조회() {
        List<CounselAnswer> result = counselAnswerRepository.findByCounselCounselId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("CounselAnswer counselId로 전체 삭제")
    void counselAnswer_counselId로_전체_삭제() {
        counselAnswerRepository.save(CounselAnswer.builder()
                .user(user).counsel(counsel).answer("답변1").build());
        counselAnswerRepository.save(CounselAnswer.builder()
                .user(user).counsel(counsel).answer("답변2").build());
        em.flush();
        em.clear();

        counselAnswerRepository.deleteByCounselCounselId(counsel.getCounselId());
        em.flush();
        em.clear();

        List<CounselAnswer> result = counselAnswerRepository.findByCounselCounselId(counsel.getCounselId());
        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════
    // CounselImgRepository 테스트
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("CounselImg 저장 후 counselId로 목록 조회")
    void counselImg_counselId로_조회() {
        counselImgRepository.save(CounselImg.builder().counsel(counsel).image("images/img1.jpg").build());
        counselImgRepository.save(CounselImg.builder().counsel(counsel).image("images/img2.jpg").build());
        em.clear();

        List<CounselImg> result = counselImgRepository.findByCounselCounselId(counsel.getCounselId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(img -> img.getCounsel().getCounselId().equals(counsel.getCounselId()));
    }

    @Test
    @DisplayName("CounselImg 없는 counselId 조회 시 빈 리스트")
    void counselImg_없는_counselId_조회() {
        List<CounselImg> result = counselImgRepository.findByCounselCounselId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("CounselImg counselId로 전체 삭제")
    void counselImg_counselId로_전체_삭제() {
        counselImgRepository.save(CounselImg.builder().counsel(counsel).image("images/img1.jpg").build());
        counselImgRepository.save(CounselImg.builder().counsel(counsel).image("images/img2.jpg").build());
        em.flush();
        em.clear();

        counselImgRepository.deleteByCounselCounselId(counsel.getCounselId());
        em.flush();
        em.clear();

        List<CounselImg> result = counselImgRepository.findByCounselCounselId(counsel.getCounselId());
        assertThat(result).isEmpty();
    }
}
