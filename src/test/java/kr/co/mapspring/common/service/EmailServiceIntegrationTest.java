package kr.co.mapspring.common.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManagerFactory;
import kr.co.mapspring.global.config.AsyncConfig;
import kr.co.mapspring.place.repository.LearningSessionRepository;


@RecordApplicationEvents // 발송 이벤트 기록 (선택 사항)
@Import({CommonService.class, AsyncConfig.class}) // 이메일 관련 설정과 서비스만 로드
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml") // 이메일 계정 정보가 있는 설정 파일
public class EmailServiceIntegrationTest {

	@Autowired
    private JavaMailSender mailSender;  // EmailService 대신 직접 주입

	@MockitoBean
    private LearningSessionRepository learningSessionRepository;
	
	@MockitoBean
	private EntityManagerFactory entityManagerFactory;
	
    @Test
    @DisplayName("실제 이메일 발송 테스트")
    void 실제_이메일_발송() throws Exception {
        // @Async 없이 직접 발송
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("syjk7504@gmail.com");
        helper.setTo("syjk7503@naver.com");  // ← 실제 이메일
        helper.setSubject("Mapingo 테스트 메일");
        helper.setText("<h2>테스트 발송입니다.</h2>", true);

        mailSender.send(message);
        System.out.println("이메일 발송 완료!");
        Thread.sleep(5000);
    }
}
