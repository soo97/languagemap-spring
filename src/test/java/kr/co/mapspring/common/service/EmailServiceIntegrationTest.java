package kr.co.mapspring.common.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;

import jakarta.mail.internet.MimeMessage;


@SpringBootTest  // 전체 컨텍스트 로드
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
	    DataSourceAutoConfiguration.class, 
	    HibernateJpaAutoConfiguration.class,
	    DataSourceTransactionManagerAutoConfiguration.class
	})
@DisplayName("이메일 실제 발송 테스트")
public class EmailServiceIntegrationTest {

	@Autowired
    private JavaMailSender mailSender;  // EmailService 대신 직접 주입

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
